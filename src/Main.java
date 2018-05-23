import java.util.Arrays;

public class Main {
	private int populationSize; 			// the size of the population
	private int generation;				// the current generation
	private int generations = 100; 		// max number of generations
	
	private City[] cities;				// new city array (implementing movement)
	private City[] originalCities; 		// the past city array
	private Chromosome[] population; 

	private double genMin;				// to store the minimum cost of all generations
	
	private boolean DO_PRINT; 			// to print progress or not
	
	/* Setting EA parameters */
	
	// Simulated Annealing
    static int nRuns = 7000; 			// max number of runs for the SA search
    static double startTemp = 1.0;		// initial temperature
    static double alpha = 0.996;			// alpha parameter (cooling rate)

    // Selection
    static int tournamentSize = 3;		// size of tournament
    static int choosePerTournament = 1;	// # winners per tournament
    
    // Crossover
    static int crossoverPoints = 1; 		// number of crossoverpoints (N as in N-point-Crossover)
    static double crossoverRate = 0.8;	// probability that a crossover will take place

    // Mutation
    static double mutationProb = 0.2; 	// probability that a mutation will take place

    
	
	/**
	 * Constructor
	 * @param cities Array of City objects (subject to dynamical movement)
	 * @param originalCities Array of City objects
	 * @param populationSize Size of the population
	 * @param DO_PRINT If progress is to be printed
	 */
	public Main(City[] cities, City[] originalCities, int populationSize, boolean DO_PRINT) {
		this.cities = cities; 
		this.originalCities = originalCities; 
		this.populationSize = populationSize;
		this.DO_PRINT = DO_PRINT; 
		
		this.run(); 
	}
	
	public double getGenMin() { return genMin; }
	
	/**
	 * Initialize population
	 */
	private void initPopulation() {
		this.population = new Chromosome[populationSize];
		for (int i = 0; i < populationSize; i++) {
    			this.population[i] = new Chromosome(cities);
		}
	}
	
    /**
     * Function to breed a child from two parent solutions using crossover
     * @param parent1 the first parent solution
     * @param parent2 the second parent solution
     * @param cityList list of all city objects
     * @param crossoverRate probability that the crossover will take place
     * @return child solution
     */
	private static Chromosome breed(Chromosome parent1, Chromosome parent2, City [] cityList, double crossoverRate){
		int[] childIndexes; 

		double sampledProb = TSP.randomGenerator.nextDouble();
		if (sampledProb > crossoverRate) {
			childIndexes = parent1.getCityIndexes();
		} else {
			childIndexes = Recombination.nPointCrossover(crossoverPoints, parent1.getCityIndexes(), parent2.getCityIndexes());
		}

		Chromosome child = new Chromosome(childIndexes, cityList);
		return child;
	}

	/**
	 * Mutates a Chromosome.
	 * @param original the chromosome to mutate
	 * @param cityList list of the City objects
	 * @param mutationProb the probability that the mutation will take place
	 * @return
	 */
	private static Chromosome mutate(Chromosome original, City[] cityList, double mutationProb) {
		int[] newCityIndexes = original.getCityIndexes();
		double sampledProb = TSP.randomGenerator.nextDouble();
		if (sampledProb <= mutationProb) {
			newCityIndexes = Mutation.mutateInversion(original.getCityIndexes());
		}

		return new Chromosome(newCityIndexes,cityList);
	}

	/**
	 * Determines whether all individuals in a population are valid by 
	 * checking the number of unique indexes in the individual's city indexes.
	 * @param chromosomes the population to check
	 * @return true if there are 50 unique cities in every individual
	 */
    private static boolean validatePopulation(Chromosome[] chromosomes) {
	    	for (Chromosome c : chromosomes) {
	    		if (Arrays.stream(c.getCityIndexes()).distinct().count() != c.getCityIndexes().length) { return false; }
	    	}
		return true;
    }
	
	private Chromosome[] evolve(Chromosome[] population, City[] cityList) {
		Chromosome[] newPopulation = new Chromosome[population.length];

		// ** Selection **
		//Chromosome[] parents = Selection.FitnessProportionateSelection(population);
	    Chromosome[] parents = Selection.DeterministicTournamentSelection(population, tournamentSize, choosePerTournament);

	    // ** Recombination **
	    Chromosome[] children = new Chromosome[population.length];
	    Chromosome parent1, parent2, child;
	    

	    for (int i=0;i<population.length;i++) {
	    		parent1 = parents[i];
	    		parent2 = parents[TSP.randomGenerator.nextInt(population.length)];

	    		// ** Recombination **
	    		child = breed(parent1, parent2, cityList, crossoverRate);
	    		
	    		// ** Mutation **
	    		child = mutate(child, cityList, mutationProb);
	    		
	    		children[i] = child;
	    }
	    
	    // ** Local search using Simulated Annealing **
	    // Do this only if best child == best parent
	    Arrays.sort(parents);
	    Arrays.sort(children);
	    
	    if (children[0].getCost() < parents[0].getCost()) {
	    		children[0] = SimulatedAnnealing.simulatedAnnealing(nRuns, startTemp, alpha, children[0], cityList);
	    }
	    
	    // ** Survival / Replacement **
	    newPopulation = Survival.GenerationalBasedSurvival(children);
	    
	    return newPopulation;
	}
	
	/**
	 * Run-function for iterating through all generations.
	 */
	private void run() {
		generation = 1; 
		genMin = 0.0; 
		
		initPopulation();
		while (generation <= generations) {
	        	population = evolve(population, cities);
	        	Arrays.sort(population);
	        	
	        	TSP.chromosomes = population;
	        	if (population[0].getCost() < genMin || genMin == 0) {
	        		genMin = population[0].getCost();
	        		if (TSP.DISPLAY) { TSP.updateGUI();}
	        	}
	        	
	        	if(generation % 5 == 0 ) {
	        		cities = TSP.MoveCities(originalCities);
	        	}
	        	if (DO_PRINT) {
	        		TSP.print(TSP.DISPLAY, "Gen: " + generation + " Cost: " + population[0].getCost());
	        	}
//	        	if (TSP.DISPLAY) {
//	        		TSP.updateGUI();
//	        	}
	        	
	        	generation++; 
		}
		
		TSP.writeLog(this.genMin + "");
	}
	
}
