import java.util.Arrays;

public abstract class Survival {

  public static Chromosome[] GenerationalBasedSurvival(Chromosome[] chromosomes) {
    return chromosomes;
  }

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
