package Logic;

import Cilly.TSPAlgo;
import Cilly.TSPLoader;
import supportMethods.MyEdge;
import supportMethods.Point;

import java.io.IOException;
import java.util.*;

public class GraphAlgo {
    /*static Scanner scanner = new Scanner(System.in);
   private static String filePath = scanner.nextLine();;*/
    private List<Point> citys;
    private int populationSize;
    private double crossoverRate;
    private double mutationRate;
    private int tournamentSize;
    private int maxGenerations;

    public GraphAlgo(List<Point> citys, int populationSize, double crossoverRate, double mutationRate,
                     int tournamentSize, int maxGenerations) {
        this.citys = citys;
        this.populationSize = populationSize;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.tournamentSize = tournamentSize;
        this.maxGenerations = maxGenerations;
    }

    public List<MyEdge> solveTSP() {
        List<List<MyEdge>> population = initializePopulation();

        for (int generation = 0; generation < maxGenerations; generation++) {
            List<List<MyEdge>> nextGeneration = new ArrayList<>();

            while (nextGeneration.size() < populationSize) {
                List<MyEdge> parent1 = tournamentSelection(population);
                List<MyEdge> parent2 = tournamentSelection(population);
                List<MyEdge> offspring = crossover(parent1, parent2);
                mutate(offspring);
                nextGeneration.add(offspring);
            }

            population = nextGeneration;
        }

        return getBestIndividual(population);
    }

    private List<List<MyEdge>> initializePopulation() {
        List<List<MyEdge>> population = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            List<Point> individual = citys;
            Collections.shuffle(individual);
            population.add(this.PointToEdge(individual));
        }

        return population;
    }

    private List<MyEdge> tournamentSelection(List<List<MyEdge>> population) {//Wählt tournamentSize aus, sucht daraus das beste Individuum
        Random random = new Random();
        List<Integer> tournament = new ArrayList<>();
        for (int i = 0; i < tournamentSize; i++) {
            tournament.add(random.nextInt(population.size()));
        }
        int bestIndividualIndex = tournament.get(0);
        for (Integer index : tournament) {
            if (getTotalDistance(population.get(index)) < getTotalDistance(population.get(bestIndividualIndex))) {
                bestIndividualIndex = index;
            }
        }
        return population.get(bestIndividualIndex);
    }

    private List<MyEdge> crossover(List<MyEdge> parent1, List<MyEdge> parent2) {
        List<Point> offspring = new ArrayList<>(this.EdgeToPoint(parent1));
         List<Point> parent2Point=new ArrayList<>(this.EdgeToPoint(parent2));
        if (Math.random() < crossoverRate) {
            int crossoverPoint1 = new Random().nextInt(parent1.size());
            int crossoverPoint2 = new Random().nextInt(parent1.size());

            int startPoint = Math.min(crossoverPoint1, crossoverPoint2);
            int endPoint = Math.max(crossoverPoint1, crossoverPoint2);

            List<Point> childSegment = parent2Point.subList(startPoint, endPoint);
            List<Point> remainingCities = new ArrayList<>(this.EdgeToPoint(parent1));
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

        return this.PointToEdge(offspring);
    }

    private void mutate(List<MyEdge> individual) {
        List<Point> routeNoDistance=this.EdgeToPoint(individual);
        if (Math.random() < mutationRate) {
            int index1 = new Random().nextInt(routeNoDistance.size());
            int index2 = new Random().nextInt(routeNoDistance.size());
            Collections.swap(routeNoDistance, index1, index2);
            individual=this.PointToEdge(routeNoDistance);
        }
    }

    public List<Point>EdgeToPoint(List<MyEdge> route){
        List<Point> routeNoDistances=new ArrayList<>();
        for (int i = 0; i < route.size(); i++) {
            routeNoDistances.add(route.get(i).getA());
        }
        return routeNoDistances;
    }

    public List<MyEdge>PointToEdge(List<Point> routeNoDistances){
        List<MyEdge> route=new ArrayList<>();
        for (int j = 0; j < routeNoDistances.size(); j++) {
            if (j!=routeNoDistances.size()-1) {
                route.add(new MyEdge(routeNoDistances.get(j), routeNoDistances.get(j + 1)));
            } else {
                route.add(new MyEdge(routeNoDistances.get(j), routeNoDistances.get(0)));
            }
        }
        return route;
    }

    public int getTotalDistance(List<MyEdge> individual) {
        int totalDistance = 0;
        for (int i = 0; i < individual.size() ; i++) {
            MyEdge partIndividual=individual.get(i);
            totalDistance += partIndividual.calculateDistance();
        }
        return totalDistance;
    }

    private List<MyEdge> getBestIndividual(List<List<MyEdge>> population) {
        List<MyEdge> bestIndividual = population.get(0);
        for (List<MyEdge> individual : population) {
            if (getTotalDistance(individual) < getTotalDistance(bestIndividual)) {
                bestIndividual = individual;
            }
        }
        return bestIndividual;
    }
    private void setParameterMenue() {//Setztung der Paramter durch den Nutzer in Form eines Menps
        Scanner menuScanner = new Scanner(System.in);
        String Eingabe;
        System.out.println("Dies ist das Menü, geben Sie bitte Ihre Werte ein. Falls Sie das nicht machen werden Standartwerte genutzt");
        do {
            System.out.println("Einwohnerzahl pro Generation=e\nGenerationen Anzahl=g\nKreuzungsrate=k\nMutationsrate=m\nTurniergröße=t\nParameter Ausgeben=p\nEingabe Beenden=Q");
            Eingabe = menuScanner.nextLine();
            switch (Eingabe) {
                case "e"://Einwohnerzahl pro Generation
                case "E":
                    System.out.println("Bitte geben Sie die Einwohneranzahl ein oder Z für zurück");
                    Eingabe = menuScanner.nextLine();
                    if (!Eingabe.equals("z") && !Eingabe.equals("Z")) {
                        this.populationSize=sicherStringZuInt(Eingabe);
                    }
                    break;
                case "g"://Anzahl der Generationen
                case "G":
                    System.out.println("Bitte geben Sie die Generationenanzahl ein oder Z für zurück");
                    Eingabe = menuScanner.nextLine();
                    if (!Eingabe.equals("z") && !Eingabe.equals("Z")) {
                        this.maxGenerations=sicherStringZuInt(Eingabe);
                    }
                    break;
                case "k":
                case "K"://Kreuzungsrate
                    System.out.println("Bitte geben Sie die Kreuzungsrate ein oder Z für zurück");
                    Eingabe = menuScanner.nextLine();
                    if (!Eingabe.equals("z") && !Eingabe.equals("Z")) {
                        this.crossoverRate=sicherStringZuDouble(Eingabe);
                    }
                    break;
                case "m"://Mutationsrate
                case "M":
                    System.out.println("Bitte geben Sie die Mutationsrate ein oder Z für zurück");
                    Eingabe = menuScanner.nextLine();
                    if (!Eingabe.equals("z") && !Eingabe.equals("Z")) {
                        this.mutationRate=sicherStringZuDouble(Eingabe);
                    }
                    break;
                case "t"://Turniergröße
                case "T":
                    System.out.println("Bitte geben Sie die Turniergröße ein oder Z für zurück");
                    Eingabe = menuScanner.nextLine();
                    if (!Eingabe.equals("z") && !Eingabe.equals("Z")) {
                        this.tournamentSize=(sicherStringZuInt(Eingabe));
                    }
                    break;
                case "p"://Parameter Ausgeben
                case "P":
                    this.alleParameterAusgabe();
                    break;
                case "q"://Quit
                case "Q":
                    break;
                default:
                    System.out.println("Eingabe nicht erkannt bitte versuchen sie es erneut");
            }
        } while (!Eingabe.equals("q") && !Eingabe.equals("Q"));
        menuScanner.close();
        System.out.println("Eingabe Beendet");
    }

    private void alleParameterAusgabe() {
        System.out.println("Einwohnerzahl:" + this.populationSize);
        System.out.println("Kreuzungsrate:" + this.crossoverRate);
        System.out.println("Mutationsrate:" + this.mutationRate);
        System.out.println("Turniergröße:" + this.tournamentSize);
        System.out.println("Anzahl der Generationen:" + this.maxGenerations);
    }

    private static Integer sicherStringZuInt(String str) {//menü falsche eingabe des nUtzers abfangen
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            System.out.println("Ungültige Eingabe: " + str);
            return null;
        }
    }

    private static Double sicherStringZuDouble(String str) {//menü falsche eingabe des nUtzers abfangen
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            System.out.println("Ungültige Eingabe: " + str);
            return null;
        }
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
