import java.awt.*;
import java.io.*;
import java.text.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.Arrays;
import java.util.ArrayList;

import javax.swing.*;

public class TSP {

	private static final int cityShiftAmount = 60; //DO NOT CHANGE THIS.

	private static int SEED = 25;
	public static Random randomGenerator = new Random(SEED);

	private static boolean RUN_PARALLEL;
	protected static boolean DISPLAY; 
	private static boolean DO_PRINT = false; 

    /**
     * How many cities to use.
     */
    protected static int cityCount;

    /**
     * How many chromosomes to use.
     */
    protected static int populationSize = 100; 

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
    * Frame to display cities and paths
    */
    private static JFrame frame;

    /**
     * Integers used for statistical data
     */
    private static double min;
    private static double avg;
    private static double max;
    private static double sum;

    /**
     * Width and Height of City Map
     */
    private static int width = 600;
    private static int height = 600;


    private static Panel statsArea;
    protected static TextArea statsText;


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

    /*
     *  Deals with printing same content to System.out and GUI
     */
    public static void print(boolean guiEnabled, String content) {
        if(guiEnabled) {
            statsText.append(content + "\n");
        }

        System.out.println(content);
    }

    public static void evolve() {
    		chromosomes = Evolution.Evolve(chromosomes,cities);
    }

    /**
     * Update the display
     */
    public static void updateGUI() {
        Image img = frame.createImage(width, height);
        Graphics g = img.getGraphics();
        FontMetrics fm = g.getFontMetrics();

        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);

        if (true && (cities != null)) {
            for (int i = 0; i < cityCount; i++) {
                int xpos = cities[i].getx();
                int ypos = cities[i].gety();
                g.setColor(Color.green);
                g.fillOval(xpos - 5, ypos - 5, 10, 10);
            }

            g.setColor(Color.gray);
            for (int i = 0; i < cityCount; i++) {
                int icity = chromosomes[0].getCity(i);
                if (i != 0) {
                    int last = chromosomes[0].getCity(i - 1);
                    g.drawLine(
                        cities[icity].getx(),
                        cities[icity].gety(),
                        cities[last].getx(),
                        cities[last].gety());
                }
            }

            int homeCity = chromosomes[0].getCity(0);
            int lastCity = chromosomes[0].getCity(cityCount - 1);

            //Drawing line returning home
            g.drawLine(
                    cities[homeCity].getx(),
                    cities[homeCity].gety(),
                    cities[lastCity].getx(),
                    cities[lastCity].gety());
        }
        frame.getGraphics().drawImage(img, 0, 0, frame);
    }

    /**
     * Load City objects from file
     * @param filename path of the file
     * @param citiesArray array where the City objects should be stored
     * @return City-array
     */
    
    protected static City[] LoadCitiesFromFile(String path, City[] cityArray) {
    		ArrayList<City> cities = new ArrayList<City>();
    		
    		try {
    			Scanner scanner = new Scanner(new File(path));
    			while (scanner.hasNext()){
        			String next = scanner.nextLine(); 
        			String[] coordinates = next.split(", ");
        			cities.add(new City(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])));
        		}
        		scanner.close();
    		} catch (FileNotFoundException fne){
    			System.err.println("Error in LoadCitiesFromFile\n" + fne.getMessage());
    		} catch (Exception e) {
    			System.err.println("Error in LoadCitiesFromFile\nError reading file line by line:" + e.getMessage());
    		}
    		
    		cityArray = new City[cities.size()];
    		return cities.toArray(cityArray);
    }

    /**
     * Move the position of the cities
     * @param cities array of city objects
     * @return new city array with cities moved.
     */
    protected static City[] MoveCities(City[] cities) {
    	City[] newPositions = new City[cities.length];
        //Random randomGenerator = new Random();

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
        DISPLAY = false;
        RUN_PARALLEL = false;
        String formatMessage = "Usage: java TSP 1 --gui --parallel \n java TSP [Runs] [Flags]\n" + 
        		"\"--gui\" enables gui \n\"--parallel\" runs the program in parallel (4 cores)";

        if (args.length < 1) {
            System.out.println("Please enter the arguments");
            System.out.println(formatMessage);
            DISPLAY = false;
        } else {
            if (args.length > 1) {
	        		if (Utils.find(args, "--gui") >= 0 || args[1].equals("y")) {
	        			DISPLAY = true;
	        		}
            		if (Utils.find(args, "--parallel") >= 0) {
            			RUN_PARALLEL = true;
            			DISPLAY = false;
            		}
            		if (Utils.find(args,  "y") >= 0) {
            			RUN_PARALLEL = false;
            			DISPLAY = true; 
            		}
            }

            try {
                cityCount = 50;
                populationSize = 100;
                runs = Integer.parseInt(args[0]);

                if(DISPLAY) {
                    frame = new JFrame("Traveling Salesman");
                    statsArea = new Panel();

                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setSize(width + 300, height);
                    frame.setResizable(false);
                    frame.setLayout(new BorderLayout());

                    statsText = new TextArea(35, 35);
                    statsText.setEditable(false);

                    statsArea.add(statsText);
                    frame.add(statsArea, BorderLayout.EAST);

                    frame.setVisible(true);
                }

                min = 0;
                avg = 0;
                max = 0;
                sum = 0;

                double startTime, endTime, duration;
                
                String filename = new File("").getAbsolutePath() +"/cityList.txt";
                originalCities = cities = LoadCitiesFromFile(filename, cities);
                writeLog("Run Stats for experiment at: " + currentTime);

                startTime = System.currentTimeMillis();

				if (RUN_PARALLEL) {
					System.out.println("Parallel started. Runs = " + runs + ".");
					IntStream.range(0, runs).parallel().forEach(i -> {
						Main main = new Main(Arrays.copyOf(cities, cities.length), originalCities, populationSize, false);

						max = (main.getGenMin() > max || max == 0) ? main.getGenMin() : max;
						min = (main.getGenMin() < min || min == 0) ? main.getGenMin() : min;
						sum +=  main.getGenMin();
					});
				} 
				
				else {
					for (int run = 1; run <= runs; run++) {
						
						if (DO_PRINT) print(DISPLAY, "Run " + run);
						
						Main main = new Main(Arrays.copyOf(cities, cities.length), originalCities, populationSize, DO_PRINT);
						max = (main.getGenMin() > max || max == 0) ? main.getGenMin() : max;
						min = (main.getGenMin() < min || min == 0) ? main.getGenMin() : min;
						sum +=  main.getGenMin();
						
	                    if (DO_PRINT) print(DISPLAY, "");
					}
				}

                endTime = System.currentTimeMillis();
                duration = (endTime - startTime)*1E-3;
                double avgTimePerRun = (RUN_PARALLEL) ? (double) duration*4 / runs : (double) duration / runs;

                avg = sum / runs;
                print(DISPLAY, "Statistics after " + runs + " runs");
                print(DISPLAY, "Total time: " + duration + "s, Time per run: " + avgTimePerRun + "s");
                print(DISPLAY, "Solution found after " + generation + " generations." + "\n");
                print(DISPLAY, "Statistics of minimum cost from each run \n");
                print(DISPLAY, "Lowest: " + min + "\nAverage: " + avg + "\nHighest: " + max + "\n");

            } catch (NumberFormatException e) {
                System.out.println("Please ensure you enter integers for cities and population size");
                System.out.println(formatMessage);
            }
        }
    }
}
