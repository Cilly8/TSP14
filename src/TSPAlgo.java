import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class TSPAlgo {
    /*static Scanner scanner = new Scanner(System.in);
    private static String filePath = scanner.nextLine();;*/
    private double[][] distances;
    private int populationSize;
    private double crossoverRate;
    private double mutationRate;
    private int tournamentSize;
    private int maxGenerations;

    public TSPAlgo(double[][] distances, int populationSize, double crossoverRate, double mutationRate,
                   int tournamentSize, int maxGenerations) {
        this.distances = distances;
        this.populationSize = populationSize;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.tournamentSize = tournamentSize;
        this.maxGenerations = maxGenerations;
    }

    public List<Integer> solveTSP() {
        List<List<Integer>> population = initializePopulation();

        for (int generation = 0; generation < maxGenerations; generation++) {
            List<List<Integer>> nextGeneration = new ArrayList<>();

            while (nextGeneration.size() < populationSize) {
                List<Integer> parent1 = tournamentSelection(population);
                List<Integer> parent2 = tournamentSelection(population);
                List<Integer> offspring = crossover(parent1, parent2);
                mutate(offspring);
                nextGeneration.add(offspring);
            }

            population = nextGeneration;
        }

        return getBestIndividual(population);
    }

    private List<List<Integer>> initializePopulation() {
        List<List<Integer>> population = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            List<Integer> individual = new ArrayList<>();
            for (int j = 0; j < distances.length; j++) {
                individual.add(j);
            }
            Collections.shuffle(individual);
            population.add(individual);
        }

        return population;
    }

    private List<Integer> tournamentSelection(List<List<Integer>> population) {
        Random random = new Random();
        List<Integer> tournament = new ArrayList<>();
        for (int i = 0; i < tournamentSize; i++) {
            tournament.add(random.nextInt(population.size()));
        }
        int bestIndividualIndex = tournament.get(0);
        for (Integer index : tournament) {
            if (fitness(population.get(index)) < fitness(population.get(bestIndividualIndex))) {
                bestIndividualIndex = index;
            }
        }
        return population.get(bestIndividualIndex);
    }

    private List<Integer> crossover(List<Integer> parent1, List<Integer> parent2) {
        List<Integer> offspring = new ArrayList<>(parent1);

        if (Math.random() < crossoverRate) {
            int crossoverPoint1 = new Random().nextInt(parent1.size());
            int crossoverPoint2 = new Random().nextInt(parent1.size());

            int startPoint = Math.min(crossoverPoint1, crossoverPoint2);
            int endPoint = Math.max(crossoverPoint1, crossoverPoint2);

            List<Integer> childSegment = parent2.subList(startPoint, endPoint);
            List<Integer> remainingCities = new ArrayList<>(parent1);
            remainingCities.removeAll(childSegment);

            int insertIndex = 0;
            for (int i = 0; i < parent1.size(); i++) {
                if (i >= startPoint && i < endPoint) {
                    offspring.set(i, childSegment.get(i - startPoint));
                } else {
                    offspring.set(i, remainingCities.get(insertIndex++));
                }
            }
        }

        return offspring;
    }

    private void mutate(List<Integer> individual) {
        if (Math.random() < mutationRate) {
            int index1 = new Random().nextInt(individual.size());
            int index2 = new Random().nextInt(individual.size());
            Collections.swap(individual, index1, index2);
        }
    }

    private int fitness(List<Integer> individual) {
        int totalDistance = 0;
        for (int i = 0; i < individual.size() - 1; i++) {
            totalDistance += distances[individual.get(i)][individual.get(i + 1)];
        }
        totalDistance += distances[individual.get(individual.size() - 1)][individual.get(0)]; // Return to the starting city
        return totalDistance;
    }

    private List<Integer> getBestIndividual(List<List<Integer>> population) {
        List<Integer> bestIndividual = population.get(0);
        for (List<Integer> individual : population) {
            if (fitness(individual) < fitness(bestIndividual)) {
                bestIndividual = individual;
            }
        }
        return bestIndividual;
    }

    public static void main(String[] args) throws IOException {
        double[][] distancesB = TSPLoader.distanceMatrix("src\\berlin52.tsp", 52);
        double[][] distancesA = TSPLoader.distanceMatrix("src\\gr229.tsp", 229);
        int populationSize = 50;
        double crossoverRate = 0.8;
        double mutationRate = 0.2;
        int tournamentSize = 5;
        int maxGenerations = 1000;

        TSPAlgo ga = new TSPAlgo(distancesB, populationSize, crossoverRate, mutationRate,
                tournamentSize, maxGenerations);
        TSPAlgo ga2 = new TSPAlgo(distancesA, populationSize, crossoverRate, mutationRate,
                tournamentSize, maxGenerations);

        List<Integer> solution = ga.solveTSP();
        System.out.println("Best solution found: " + solution);
        System.out.println("Total distance: " + ga.fitness(solution));

        List<Integer> solution2 = ga2.solveTSP();
        System.out.println("Best solution found: " + solution2);
        System.out.println("Total distance: " + ga2.fitness(solution2));
    }
}

