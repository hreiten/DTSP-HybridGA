public abstract class SimulatedAnnealing {
	
	/**
	 * Decides if a proposed solution should be accepted given a probability
	 * @param acceptProbability the probability of accepting the candidate
	 * @return boolean
	 */
	public static boolean shouldAcceptCandidate(double acceptProbability) {
		double sampledProb = TSP.randomGenerator.nextDouble(); 
		if (sampledProb < acceptProbability) {
			return true; 
		}
		
		return false;
	}
	
	/**
	 * Performs the simulated annealing search
	 * @param nRuns maximum number of iterations
	 * @param startTemp the initial temperature
	 * @param alpha the cooling parameter
	 * @param original the initial chromosome
	 * @param cityList list of City Objects
	 * @return best candidate solution found (lowest cost)
	 */
	public static Chromosome simulatedAnnealing(int nRuns, double startTemp, double alpha, Chromosome original, City[] cityList) {
		double annealTemp = startTemp; 
		double acceptProb;

		Chromosome bestCandidate = new Chromosome(original.getCityIndexes(), cityList);
		Chromosome candidate = new Chromosome(original.getCityIndexes(), cityList);
		
		int run = 0;
		while (run < nRuns) {
			
			// propose a candidate solution by mutation
			int[] mutatedIndexes = Mutation.mutateInversion(candidate.cityIndexes);
			Chromosome mutatedCandidate = new Chromosome(mutatedIndexes,cityList);
			
			// calculate new temperature and accept probability based on the candidates fitness
			annealTemp = startTemp * Math.pow(alpha, run);
			acceptProb = Math.exp(-(mutatedCandidate.getCost() - candidate.getCost()) / annealTemp);
			
			// if solution passes, then update candidate solution
			if (shouldAcceptCandidate(acceptProb)) {
				candidate = new Chromosome(mutatedCandidate.getCityIndexes(), cityList);
				
				// if better fitness than best, then update
				if (candidate.getCost() < bestCandidate.getCost()) {
					bestCandidate = new Chromosome(candidate.getCityIndexes(), cityList);
				}
			}
			run++; 
		}
		
		return bestCandidate;
	}
	
}
