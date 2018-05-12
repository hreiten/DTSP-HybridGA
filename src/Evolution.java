import java.util.Arrays;
import java.util.Random;

class Evolution{	

	// simulated annealing variables
    static int nRuns = 10000;
    static double startTemp = 8;
    static double alpha = 0.995;
    
    // crossover variables
    static double crossoverRate = 0.6;
    
    // mutation variables
    static double mutationProb = 0.01;
    
    // selection variables
    static int tournamentSize = 3; 
    static int choosePerTournament = 1; 

    public static boolean validatePopulation(Chromosome[] chromosomes) {
	    	for (Chromosome c : chromosomes) {
	    		if (Arrays.stream(c.getCityIndexes()).distinct().count() != c.getCityIndexes().length) { return false; }
	    	}
		return true;
    }
    
	public static Chromosome[] Evolve(Chromosome[] population, City[] cityList){   
		Chromosome[] newPopulation = new Chromosome[population.length];
		
		// ** Selection **
	    Chromosome[] parents = Selection.DeterministicTournamentSelection(population, tournamentSize, choosePerTournament);
	    
	    // ** Recombination **
	    Chromosome[] children = new Chromosome[population.length];
	    Chromosome parent1, parent2, child;
	    
	    for (int i=0;i<population.length;i++) {
	    		parent1 = parents[i];
	    		parent2 = parents[TSP.randomGenerator.nextInt(population.length)];
	    		
	    		child = Recombination.Breed(parent1, parent2, cityList, crossoverRate);
	    		// ** Local search using Simulated Annealing ** 
	    		child = SimulatedAnnealing.simulatedAnnealing(nRuns, startTemp, alpha, child, cityList);
	    		
	    		// ** Mutation ** 
	    		int[] newIndexes = Mutation.mutateSwap(child.getCityIndexes(), mutationProb);
	    		child.setCities(newIndexes);
	    		
	    		children[i] = child;
	    }
	    
	    // ** Survival / Selection **
	    newPopulation = Survival.ElitistSurvival(parents, children, 1);
	    if(!validatePopulation(newPopulation)) {
	    		throw new IllegalStateException("# unique elements in new population is != " + newPopulation[0].getCityIndexes().length);
	    }
	    return newPopulation;
	}

}