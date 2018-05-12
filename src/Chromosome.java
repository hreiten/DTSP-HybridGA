class Chromosome implements Comparable<Chromosome> {

    /**
     * The list of cities, which are the genes of this chromosome.
     */
    protected int[] cityIndexes;

    /**
     * The cost of following the cityList order of this chromosome.
     */
    protected double cost;

    /**
     * @param cities The order that this chromosome would visit the cities.
     */
    Chromosome(int[] cityIndexes, City[] cities) {
    		this.cityIndexes = cityIndexes; 
        calculateCost(cities);
    }
    
    Chromosome(City[] cities) {
    		int[] cityIndexes = new int[cities.length];
		for (int i=0; i<cities.length; i++) {
			cityIndexes[i] = i;
		}
		this.cityIndexes = cityIndexes; 
		this.shuffleCityIndexes();

		calculateCost(cities);
    }
    
    void shuffleCityIndexes() {
        for (int y = 0; y < this.cityIndexes.length; y++) {
            int temp = this.cityIndexes[y];
            int randomNum = TSP.randomGenerator.nextInt(this.cityIndexes.length);
            this.cityIndexes[y] = this.cityIndexes[randomNum];
            this.cityIndexes[randomNum] = temp;
        }
    }

    /**
     * Calculate the cost of the specified list of cities.
     *
     * @param cities A list of cities.
     */
    void calculateCost(City[] cities) {
        cost = 0;
        for (int i = 0; i < cityIndexes.length - 1; i++) {
            double dist = cities[cityIndexes[i]].proximity(cities[cityIndexes[i + 1]]);
            cost += dist;
        }

        cost += cities[cityIndexes[0]].proximity(cities[cityIndexes[cityIndexes.length - 1]]); //Adding return home
    }

    /**
     * Get the cost for this chromosome. This is the amount of distance that
     * must be traveled.
     */
    double getCost() {
        return cost;
    }
    
    int[] getCityIndexes() {
    		return this.cityIndexes;
    }

    /**
     * @param i The city you want.
     * @return The ith city.
     */
    int getCity(int i) {
        return cityIndexes[i];
    }

    /**
     * Set the order of cities that this chromosome would visit.
     *
     * @param list A list of cities.
     */
    void setCities(int[] list) {
        for (int i = 0; i < cityIndexes.length; i++) {
            cityIndexes[i] = list[i];
        }
    }

    /**
     * Set the index'th city in the city list.
     *
     * @param index The city index to change
     * @param value The city number to place into the index.
     */
    void setCity(int index, int value) {
        cityIndexes[index] = value;
    }

    /**
     * Sort the chromosomes by their cost.
     *
     * @param chromosomes An array of chromosomes to sort.
     * @param num         How much of the chromosome list to sort.
     */
    public static void sortChromosomes(Chromosome chromosomes[], int num) {
        Chromosome ctemp;
        boolean swapped = true;
        while (swapped) {
            swapped = false;
            for (int i = 0; i < num - 1; i++) {
                if (chromosomes[i].getCost() > chromosomes[i + 1].getCost()) {
                    ctemp = chromosomes[i];
                    chromosomes[i] = chromosomes[i + 1];
                    chromosomes[i + 1] = ctemp;
                    swapped = true;
                }
            }
        }
    }

	@Override
	public int compareTo(Chromosome other) {
		return (int) (this.getCost() - other.getCost());
	}
	
	@Override
	public String toString() {
		return "" + this.getCost();
	}
}
