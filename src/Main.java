import java.util.Arrays;

public class Main {
	private int populationSize; 
	private int generation;
	private int generations = 100; 
	
	private City[] cities;
	private City[] originalCities;
	private Chromosome[] population; 

	private double min;
	private double avg;
	private double max;
	private double sum;
	private double genMin;
	
	public Main(City[] cities, City[] originalCities, int populationSize) {
		this.cities = cities; 
		this.originalCities = originalCities; 
		this.populationSize = populationSize; 
		
		this.run(); 
	}
	
	public double getGenMin() { return genMin; }
	
	private void initPopulation() {
		this.population = new Chromosome[populationSize];
		for (int i = 0; i < populationSize; i++) {
    			this.population[i] = new Chromosome(cities);
		}
	}
	
	private void sortChromosomes(){
		Arrays.sort(this.population, (a,b) ->
			Double.valueOf(a.getCost()).compareTo(Double.valueOf(b.getCost())));
	}
	
	private void run() {
		generation = 1; 
		genMin = 0.0; 
		
		initPopulation();
		while (generation <= generations) {
	        	population = Evolution.Evolve(population, cities);
	        	sortChromosomes();
	        	
	        	genMin = (population[0].getCost() < genMin || genMin == 0) ? population[0].getCost() : genMin; 
	        	
	        	if(generation % 5 == 0 ) {
	        		cities = TSP.MoveCities(originalCities);
	        	}
	        	generation++; 
		}
		TSP.writeLog(this.genMin + "");
	}
	
}
