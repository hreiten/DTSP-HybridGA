import java.util.Arrays;
import java.util.function.Function;

public abstract class Selection {
	
    @FunctionalInterface
    interface Function2 <A, B, R> { 
    		public R apply (A a, B b);
    }
	
	public static Chromosome[] FitnessProportionateSelection(Chromosome[] population){
		Chromosome[] pop = Arrays.copyOf(population, population.length);

		double sum = 0; double max = 0.0; double min = 100000.0;
		for (int i=0; i<pop.length; i++) {
			double cost = pop[i].getCost();
			sum += cost;
			max = (cost > max) ? cost : max;
			min = (cost < min) ? cost : max;
		}
		
//		Function<Double,Double> probFuncSum = f -> ((double) 1/(population.length-1))*(1-f/sum);
//		Function<Double,Double> probFuncSum2 = f -> 1 - f/sum;
		Function2<Double,Double,Double> fitnessFunc = (cost,maxCost) -> ((double) 1 - cost/maxCost);
		double[] probs = new double[pop.length];
		for (int i=0; i<pop.length;i++) {
			probs[i] = fitnessFunc.apply(pop[i].getCost(), max);
		}
		
		Chromosome[] newPop = new Chromosome[pop.length];
		int added = 0; 
		while (added < newPop.length) {
			int index = TSP.randomGenerator.nextInt(pop.length);
			double sampledProb = TSP.randomGenerator.nextDouble();
			if (sampledProb <= probs[index]) {
				newPop[added] = pop[added];
				added++;
			}
		}
		
		return newPop; 
	}
	
	public static Chromosome[] DeterministicTournamentSelection(Chromosome[] population, int tournamentSize, int selectPerTournament){
		Chromosome[] newPopulation = new Chromosome[population.length];
		
		int addedElements = 0;
		Chromosome[] contestors; 
		while (addedElements < population.length) {
			contestors = new Chromosome[tournamentSize];
			for (int j=0; j<contestors.length;j++) {
				contestors[j] = population[TSP.randomGenerator.nextInt(population.length)];
			}
			Chromosome.sortChromosomes(contestors, contestors.length);
			
			int addElements = selectPerTournament; 
			if (addedElements + addElements > population.length) { addElements = population.length - addedElements; }
			
			int k=0;
			for (int i=addedElements; i<addedElements + addElements; i++) {
				newPopulation[i] = contestors[k++];
			}
			
			addedElements += addElements; 
		}
		return newPopulation;
	}
	
	public static Chromosome[] RankBasedSelection(Chromosome[] population) {
		Chromosome[] pop = Arrays.copyOf(population, population.length);
		Chromosome.sortChromosomes(pop, pop.length);
		
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
