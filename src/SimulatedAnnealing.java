public abstract class SimulatedAnnealing {
	
	public static boolean shouldAcceptCandidate(double acceptProbability) {
		double sampledProb = TSP.randomGenerator.nextDouble(); 
		if (sampledProb < acceptProbability) {
			return true; 
		}
		
		return false;
	}
	
	public static Chromosome simulatedAnnealing(int nRuns, double startTemp, double alpha, Chromosome original, City[] cityList) {
		double annealTemp = startTemp; 
		double acceptProb;
		
		Chromosome candidate = new Chromosome(original.getCityIndexes(), cityList);
		
		int run = 0;
		while (annealTemp > 1 && run < nRuns) {
			Chromosome mutatedCandidate = Mutation.mutate(candidate, cityList, 1.0);
			
			annealTemp = startTemp * Math.pow(alpha, run);
			acceptProb = Math.exp(-(mutatedCandidate.getCost() - candidate.getCost()) / annealTemp);
			
			if (shouldAcceptCandidate(acceptProb)) {
				candidate = new Chromosome(mutatedCandidate.getCityIndexes(), cityList);
			}
			run++; 
		}
		
		return candidate;
	}
	
}
