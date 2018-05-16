import java.util.Arrays;

public abstract class Recombination {
	
	/**
	 * Crossover by Cycle Crossover
	 * @param p1 first parent vector
	 * @param p2 second parent vector
	 * @return a new vector by applying cycle crossover to the two parents
	 */
	public static int[] CycleCrossover(int[] p1, int[] p2) {
		int[] child = Arrays.copyOf(p2, p2.length);
		
		int startIndex = TSP.randomGenerator.nextInt(p1.length);
		int index = startIndex; 
		
		while (true) {
			child[index] = p1[index];
			
			// calculate the next index by finding index in vec1 that has the value of vec2[index]
			int nextIndex = Utils.find(p1,p2[index]);
			index = nextIndex;
			
			// if next index is where we started, then cycle is finished
			if (index == startIndex) {
				break;
			}
		}
		
		return child;
	}
	
	
	/**
	 * Performs N-Point Crossover on two parents. Returning only one child.
	 * @param n number of crossover points
	 * @param p1 city indexes of parent 1
	 * @param p2 city indexes of parent 2
	 * @return new city indexes
	 */
	public static int[] nPointCrossover(int n, int[] p1, int[] p2) {
		int[] newIndexes = new int[p1.length];
		int[] crossoverPoints = new int[n];
		
		// first sample n crossover points
		for (int i=0; i<n; i++) {
			int sampledIndex = 0; 
			while(Utils.find(crossoverPoints, sampledIndex) >= 0 || sampledIndex == 0) {
				sampledIndex = TSP.randomGenerator.nextInt(p1.length);
			}
			crossoverPoints[i] = sampledIndex;
		}
		Arrays.sort(crossoverPoints);
		
		boolean swap = false; 
		int added = 0; int currIndex = 0;
		int[] arr1 = p1; int[] arr2 = p2; 
		
		while (added < newIndexes.length-1) {
			
			if (swap) { 
				arr1 = p2; 
				arr2 = p1; 
				swap = false; 
				currIndex = 0;
			}
			
			for (int i=0; i<p1.length;i++) {
				if (currIndex < crossoverPoints.length) {
					if (i > crossoverPoints[currIndex]) {
						currIndex++;
					}
				}

				if (currIndex % 2 == 0 && Utils.find(newIndexes, arr1[i]) < 0) {
					newIndexes[added] = arr1[i];
					added++;
				} else if (currIndex % 2 != 0 && Utils.find(newIndexes, arr2[i]) < 0) {
					newIndexes[added] = arr2[i];
					added++;
				}
			}
			
			swap = true; 
		}
		
		return newIndexes;
	}

}
