import java.nio.file.Paths;
import java.util.Arrays;

public class Test {
	
	static double[] minCosts; 
	
	public static void main(String[] args) {
		City[] cityList = new City[50];
		cityList = TSP.LoadCitiesFromFile(Paths.get(".").toAbsolutePath().normalize().toString()+"/resources/"+"CityList.txt", cityList);
		int populationSize = 100;
		int numCities = 50;
		City[] cities = Arrays.copyOf(cityList, numCities);
		
        int generations = 100;
        int runs = 2;
        minCosts = new double[runs];
        
        for (int run=1; run<=runs; run++) {
        		System.out.println("Run " + run);
        		int generation = 1;
            double genMinCost = 1000000.0;
        		
        		// initialize population
            Chromosome[] initPopulation = new Chromosome[populationSize];
            for (int x = 0; x < populationSize; x++) {
            		initPopulation[x] = new Chromosome(cities);
            }
            Chromosome[] population = Arrays.copyOf(initPopulation, initPopulation.length);
                
            // run while 
            while (generation <= generations) {
        	        	population = Evolution.Evolve(population, cities);
        	        	Chromosome.sortChromosomes(population, populationSize);
        	        	
        	        	genMinCost = (population[0].getCost() < genMinCost) ? population[0].getCost() : genMinCost; 
        	        	
        	        	generation++; 
            }
            
            minCosts[run-1] = genMinCost; 
        }
        
        double avg = 0.0; 
        for (double cost : minCosts) {
        		avg += cost/minCosts.length;
        }
        
        System.out.println(avg);

	}
	
}
