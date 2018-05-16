import java.util.Arrays;

class Evolution{

	static int simCount; 
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
     * Function to breed a child from two parent solutions using crossover
     * @param parent1 the first parent solution
     * @param parent2 the second parent solution
     * @param cityList list of all city objects
     * @param crossoverRate probability that the crossover will take place
     * @return child solution
     */
	public static Chromosome breed(Chromosome parent1, Chromosome parent2, City [] cityList, double crossoverRate){
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
	public static Chromosome mutate(Chromosome original, City[] cityList, double mutationProb) {
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
    public static boolean validatePopulation(Chromosome[] chromosomes) {
	    	for (Chromosome c : chromosomes) {
	    		if (Arrays.stream(c.getCityIndexes()).distinct().count() != c.getCityIndexes().length) { return false; }
	    	}
		return true;
    }

    /**
     * Evolving a population from one generation to the next
     * @param population the initial population
     * @param cityList list of City objects
     * @return evolved population
     */
	public static Chromosome[] Evolve(Chromosome[] population, City[] cityList){
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
	    		simCount++;
	    		children[0] = SimulatedAnnealing.simulatedAnnealing(nRuns, startTemp, alpha, children[0], cityList);
	    }
	    
	    // ** Survival / Replacement **
	    newPopulation = Survival.GenerationalBasedSurvival(children);
	    
//	    if(!validatePopulation(newPopulation)) {
//	    		throw new IllegalStateException("Number of unique elements in new population is != " + newPopulation[0].getCityIndexes().length);
//	    }
	    
	    return newPopulation;
	}

}
