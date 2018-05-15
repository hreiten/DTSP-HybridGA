import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.stream.IntStream;

public class ParallellTSP {
	
	private static final int cityShiftAmount = 60; //DO NOT CHANGE THIS.

	public static Random randomGenerator = new Random(20);

    /**
     * How many cities to use.
     */
    protected static int cityCount;

    /**
     * How many chromosomes to use.
     */
    protected static int populationSize = 100; //DO NOT CHANGE THIS.

    /**
     * The part of the population eligable for mating.
     */
    protected static int matingPopulationSize;

    /**
     * The part of the population selected for mating.
     */
    protected static int selectedParents;

    /**
     * The current generation
     */
    protected static int generation;

    /**
     * The list of cities (with current movement applied).
     */
    protected static City[] cities;

    /**
     * The list of cities that will be used to determine movement.
     */
    private static City[] originalCities;

    /**
     * The list of chromosomes.
     */
    protected static Chromosome[] chromosomes;

    /**
     * Integers used for statistical data
     */
    private static double min;
    private static double avg;
    private static double max;
    private static double sum;
    private static double genMin;

    /*
     * Writing to an output file with the costs.
     */
    protected static void writeLog(String content) {
        String filename = "results.out";
        FileWriter out;

        try {
            out = new FileWriter(filename, true);
            out.write(content + "\n");
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected static City[] LoadCitiesFromFile(String filename, City[] citiesArray) {
        ArrayList<City> cities = new ArrayList<City>();
        try
        {
            FileReader inputFile = new FileReader(filename);
            BufferedReader bufferReader = new BufferedReader(inputFile);
            String line;
            while ((line = bufferReader.readLine()) != null) {
                String [] coordinates = line.split(", ");
                cities.add(new City(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])));
            }

            bufferReader.close();

        } catch (Exception e) {
            System.out.println("Error while reading file line by line:" + e.getMessage());
        }

        citiesArray = new City[cities.size()];
        return cities.toArray(citiesArray);
    }

    protected static City[] MoveCities(City[]cities) {
    		City[] newPositions = new City[cities.length];

        for(int i = 0; i < cities.length; i++) {
        	int x = cities[i].getx();
        	int y = cities[i].gety();

            int position = randomGenerator.nextInt(5);

            if(position == 1) {
            	y += cityShiftAmount;
            } else if(position == 2) {
            	x += cityShiftAmount;
            } else if(position == 3) {
            	y -= cityShiftAmount;
            } else if(position == 4) {
            	x -= cityShiftAmount;
            }

            newPositions[i] = new City(x, y);
        }

        return newPositions;
    }

    public static void main(String[] args) {
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String currentTime  = df.format(today);

        int runs;
        String formatMessage = "Usage: java TSP [Runs]";

        if (args.length < 1) {
            System.out.println("Please enter the arguments");
            System.out.println(formatMessage);
        } else {

            try {
                cityCount = 50;
                populationSize = 100;
                runs = Integer.parseInt(args[0]);

                min = 0;
                avg = 0;
                max = 0;
                sum = 0;

                String filename = new File("").getAbsolutePath() + "/resources/cityList.txt";
                originalCities = cities = LoadCitiesFromFile(filename, cities);
                writeLog("Run Stats for experiment at: " + currentTime);

                long startTime = System.nanoTime();
                System.out.println("Parallell started with runs = " + runs);
                IntStream.range(0, runs).parallel().forEach(i -> {
                    Main main = new Main(Arrays.copyOf(cities, cities.length), originalCities, populationSize, false);

                    max = (main.getGenMin() > max || max == 0) ? main.getGenMin() : max;
                    min = (main.getGenMin() < min || min == 0) ? main.getGenMin() : min;
                    sum +=  main.getGenMin();
                  });
                
                avg = sum / runs; 
                long endTime = System.nanoTime();

                int totalDuration = (int) ((endTime - startTime)*1E-6);
                int avgTimePerGeneration = totalDuration / (runs*100);
                double avgTimePerRun = totalDuration / runs*1E-3;

                avg = sum / runs;
                System.out.println("Statistics after " + runs + " runs");
                System.out.println("Total time: " + totalDuration*1E-3 + "s, Time per run: " + avgTimePerRun + "s, Time per generation: " + avgTimePerGeneration + "ms");
                System.out.println("Solution found after " + generation + " generations." + "\n");
                System.out.println("Statistics of minimum cost from each run \n");
                System.out.println("Lowest: " + min + "\nAverage: " + avg + "\nHighest: " + max + "\n");

            } catch (NumberFormatException e) {
                System.out.println("Please ensure you enter integers for cities and population size");
                System.out.println(formatMessage);
            }
        }
    }
	
}
