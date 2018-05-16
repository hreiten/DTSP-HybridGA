import java.util.Arrays;
import java.util.function.Function;

public abstract class Selection {
	
    @FunctionalInterface
    interface Function2 <A, B, R> { 
    		public R apply (A a, B b);
    }
	
    /**
     * Probabilistically selects individuals from the population based on relative fitness
     * Probability function is P(x) = 1 - x/max
     * @param population the initial population
     * @return the selected population
     */
	public static Chromosome[] FitnessProportionateSelection(Chromosome[] population){
		
		double max = 0.0; double sum = 0.0; 
		for (int i=0; i<population.length; i++) {
			double cost = population[i].getCost();
			sum += cost; 
			max = (cost > max || max == 0.0) ? cost : max;
		}
		
		Function2<Double,Double,Double> probSum = (cost,sumCost) -> ((double) cost/sumCost);
		Function2<Double,Double,Double> probMax = (cost,maxCost) -> ((double) 1 - cost/maxCost);
		double[] probs = new double[population.length];
		for (int i=0; i<population.length;i++) {
			probs[i] = probMax.apply(population[i].getCost(), max);
		}
		
		Chromosome[] newPop = new Chromosome[population.length];
		int added = 0; 
		while (added < newPop.length) {
			int index = TSP.randomGenerator.nextInt(population.length);
			double sampledProb = TSP.randomGenerator.nextDouble();
			if (sampledProb <= probs[index]) {
				newPop[added] = population[index];
				added++;
			}
		}
		
		return newPop; 
	}
	
	/**
	 * Deterministically select from population using the tournament technique
	 * @param population the initial population
	 * @param tournamentSize the size of the tournament (# individuals in tournament)
	 * @param selectPerTournament how many winners per tournament
	 * @return selected population
	 */
	public static Chromosome[] DeterministicTournamentSelection(Chromosome[] population, int tournamentSize, int selectPerTournament){
		Chromosome[] newPopulation = new Chromosome[population.length];
		
		int addedElements = 0;
		Chromosome[] contestors; 
		while (addedElements < population.length) {
			// sample n contestors to compete in the tournament
			contestors = new Chromosome[tournamentSize];
			for (int j=0; j<contestors.length;j++) {
				contestors[j] = population[TSP.randomGenerator.nextInt(population.length)];
			}
			Arrays.sort(contestors);
			
			// find out how many from the tournament to select
			int addElements = selectPerTournament; 
			if (addedElements + addElements > population.length) { addElements = population.length - addedElements; }
			
			// add the winners to the new population
			//System.out.println("contestors are " + Arrays.toString(contestors));
			int k=0;
			for (int i=addedElements; i<addedElements + addElements; i++) {
				newPopulation[i] = contestors[k];
				//System.out.println("Added " + contestors[k]);
				k++;
			}
			
			addedElements += addElements; 
		}
		return newPopulation;
	}
	
	/**
	 * Probabilistically select individuals based on relative rank
	 * @param population the initial population
	 * @return the selected population
	 */
	public static Chromosome[] RankBasedSelection(Chromosome[] population) {
		Chromosome[] pop = Arrays.copyOf(population, population.length);
		Arrays.sort(pop);
		
		double nneg = 0.5;
		double npos = 2 - nneg; 
		int N = population.length;
		Function<Integer,Double> linearRanking = rank -> ((double) 1/N)*(nneg + (npos-nneg)*((double) rank-1)/(N-1));
		
		Chromosome[] newPop = new Chromosome[pop.length];
		int added = 0; 
		while (added < pop.length) {
			int sampledIndex = TSP.randomGenerator.nextInt(population.length);
			Chromosome target = population[sampledIndex];
			
			int targetRank = Utils.find(pop, target);
			double prob = linearRanking.apply(targetRank);
			double sampledProb = TSP.randomGenerator.nextDouble();
			if (sampledProb <= prob) {
				newPop[added++] = target; 
			}
		}
		
		return newPop; 
		
	}
	
}
