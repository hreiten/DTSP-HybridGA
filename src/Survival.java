import java.util.Arrays;

public abstract class Survival {

  public static Chromosome[] GenerationalBasedSurvival(Chromosome[] chromosomes) {
    return chromosomes;
  }

  /**
   * Replacement by elitism. Keeps n fittest individuals from original population.
   * @param originalPopulation the original population (parents)
   * @param children the population of offspring
   * @param individualsToKeep the number of fittest individuals to keep
   * @return
   */
  public static Chromosome[] ElitistSurvival(Chromosome[] originalPopulation, Chromosome[] children, int individualsToKeep) {
	  Chromosome[] population = Arrays.copyOf(originalPopulation, originalPopulation.length);
	  Chromosome.sortChromosomes(population, population.length);
	  Chromosome[] newPopulation = new Chromosome[population.length];
	  
	  for (int i=0; i < individualsToKeep; i++) {
		  newPopulation[i] = population[i];
	  }
	  
	  for (int i=individualsToKeep; i<population.length;i++) {
		  newPopulation[i] = children[i];
	  }

    return newPopulation;
  }

}
