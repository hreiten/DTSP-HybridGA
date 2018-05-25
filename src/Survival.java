import java.util.Arrays;

public abstract class Survival {
    /**
     * Let new generation completely replace the old one
     * @param chromosomes the new chromosomes to fill the new generation
     * @return new population
     */
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
    public static Chromosome[] ElitistSurvival(Chromosome[] population, Chromosome[] children, int individualsToKeep) {
        Arrays.sort(population);
        Chromosome[] newPopulation = new Chromosome[population.length];

        for (int i = 0; i < individualsToKeep; i++) {
            newPopulation[i] = population[i];
        }

        for (int i = individualsToKeep; i < population.length; i++) {
            newPopulation[i] = children[i - individualsToKeep];
        }

        return newPopulation;
    }
}
