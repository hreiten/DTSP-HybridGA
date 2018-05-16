class Chromosome implements Comparable<Chromosome>{

    /**
     * The list of cities, which are the genes of this chromosome.
     */
    protected int[] cityIndexes;

    /**
     * The cost of following the cityList order of this chromosome.
     */
    protected double cost; 

    /**
     * Constructor. 
     * @param cityIndexes the order of which city of index i will be visited
     * @param cities the list of City objects
     */
    Chromosome(int[] cityIndexes, City[] cities) {
    		this.cityIndexes = cityIndexes; 
        calculateCost(cities);
    }
    
    /**
     * Constructor. Will initialize by randomly setting cityIndexes.
     * @param cities list of city objects
     */
    Chromosome(City[] cities) {
    		int[] cityIndexes = new int[cities.length];
		for (int i=0; i<cities.length; i++) {
			cityIndexes[i] = i;
		}
		this.cityIndexes = cityIndexes; 
		this.shuffleCityIndexes();

		calculateCost(cities);
    }
    
    /**
     * Shuffles this chromosomes cityIndexes to a random order.
     */
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
     * @param cities list of City objects.
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
     * @param list A list of cities.
     */
    void setCities(int[] list) {
        for (int i = 0; i < cityIndexes.length; i++) {
            cityIndexes[i] = list[i];
        }
    }

    /**
     * Set the index'th city in the city list.
     * @param index The city index to change
     * @param value The city number to place into the index.
     */
    void setCity(int index, int value) {
        cityIndexes[index] = value;
    }
	
	@Override
	public String toString() {
		return "" + this.getCost();
	}

	@Override
	public int compareTo(Chromosome o) {
		return Double.valueOf(this.getCost()).compareTo(Double.valueOf(o.getCost()));
	}
}
