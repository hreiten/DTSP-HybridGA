import java.nio.file.Paths;
import java.util.Arrays;

public class Test {
	
	public static void main(String[] args) {
		City[] cityList = new City[50];
		cityList = TSP.LoadCitiesFromFile(Paths.get(".").toAbsolutePath().normalize().toString()+"/"+"CityList.txt", cityList);
		int populationSize = 100;
		int numCities = 50;
        int generations = 1;
        int generation = 1; 
        
        // initialize population
        City[] cities = Arrays.copyOf(cityList, numCities);
        Chromosome[] initPopulation = new Chromosome[populationSize];
        for (int x = 0; x < populationSize; x++) {
           initPopulation[x] = new Chromosome(cities);
        }
        Chromosome[] population = Arrays.copyOf(initPopulation, initPopulation.length);
        
        // run while 
        while (generation <= generations) {
	        	//population = Evolution.Evolve(population, cities);
	        	Chromosome[] newPopulation = new Chromosome[population.length];
	    		
	    		// Select parents
	    		//Chromosome[] parents = Selection.RankBasedSelection(population);
	    	    Chromosome[] parents = Selection.DeterministicTournamentSelection(population, 3, 1);
	    	    
	    	    Chromosome[] children = new Chromosome[population.length];
	    	    Chromosome parent1, parent2, child;
	    	    
	    	    for (int i=0;i<population.length;i++) {
	    	    		parent1 = parents[i];
	    	    		parent2 = parents[TSP.randomGenerator.nextInt(population.length)];
	    	    		
	    	    		int[] childIndexes = Recombination.nPointCrossover(2,parent1.getCityIndexes(), parent2.getCityIndexes());
	    	    		child = new Chromosome(childIndexes,cities);
	    	    		child = SimulatedAnnealing.simulatedAnnealing(100000, 10, 0.99, child, cityList);
	    	    		
	    	    		children[i] = Mutation.mutate(child, cityList, 1.0);
	    	    		
	    	    }
        		
        		generation++;
        }
	}
	
}
