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
	 * Run-function for iterating through all generations.
	 */
	private void run() {
		generation = 1; 
		genMin = 0.0; 
		
		initPopulation();
		while (generation <= generations) {
	        	population = Evolution.Evolve(population, cities);
	        	Arrays.sort(population);
	        	
	        	TSP.chromosomes = population;
	        	genMin = (population[0].getCost() < genMin || genMin == 0) ? population[0].getCost() : genMin; 
	        	
	        	if(generation % 5 == 0 ) {
	        		cities = TSP.MoveCities(originalCities);
	        	}
	        	if (DO_PRINT) {
	        		TSP.print(TSP.DISPLAY, "Gen: " + generation + " Cost: " + population[0].getCost());
	        	}
	        	if (TSP.DISPLAY) {
	        		TSP.updateGUI();
	        	}
	        	
	        	generation++; 
		}
		
		TSP.writeLog(this.genMin + "");
	}
	
}
