package Logic;

import supportMethods.MyEdge;
import supportMethods.Point;

import java.io.IOException;
import java.util.*;
import java.util.Collections;


public class GraphAlgo {
    private List<Point> cities;
    private int populationSize;
    private double crossoverRate;
    private double mutationRate;
    private int tournamentSize;
    private int maxGenerations;
    boolean berlin = true;

    public GraphAlgo(List<Point> cities, int populationSize, double crossoverRate, double mutationRate,
                     int tournamentSize, int maxGenerations) {
        this.cities = cities;
        this.populationSize = populationSize;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.tournamentSize = tournamentSize;
        this.maxGenerations = maxGenerations;
    }

    //TSP-LÖSUNGSMETHODE: Alle Methoden werden nacheinander aufgerufen um das TSP zu lösen
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

            // Führe (µ + λ)-Selektion durch, um die nächste Generation auszuwählen
            population = muPlusLambdaSelection(population, nextGeneration, populationSize-1);
            //population = nextGeneration;
        }
        return getBestIndividual(population);
    }

    //Initialisierung
    private List<List<MyEdge>> initializePopulation() {
        List<List<MyEdge>> population = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            List<Point> individual = cities;
            Collections.shuffle(individual);
            population.add(this.PointToEdge(individual));
        }
        return population;
    }

    //Wählt tournamentSize aus, sucht daraus das beste Individuum
    private List<MyEdge> tournamentSelection(List<List<MyEdge>> population) {
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

    //Rekombinations-Operator
    private List<MyEdge> crossover(List<MyEdge> parent1, List<MyEdge> parent2) {
        List<Point> offspring = new ArrayList<>(this.EdgeToPoint(parent1));
        List<Point> parent2Point = new ArrayList<>(this.EdgeToPoint(parent2));

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

    //Mutations-Operator
    private void mutate(List<MyEdge> individual) {
        if (Math.random() < mutationRate) {
            // Konvertiere die MyEdge Liste zu einer Point Liste
            List<Point> route = EdgeToPoint(individual);

            Random rand = new Random();
            int index1 = rand.nextInt(route.size());
            int index2 = rand.nextInt(route.size());

            //Stelle sicher, dass index1 und index2 unterschiedlich sind, um einen sinnvollen Tausch zu ermöglichen
            while (index1 == index2) {
                index2 = rand.nextInt(route.size());
            }

            //Vertauschen von zwei zufälligen Punkten/Städten in der Route
            Collections.swap(route, index1, index2);

            //Konvertiere die modifizierte Route zurück zu einer MyEdge-Liste
            List<MyEdge> mutatedIndividual = PointToEdge(route);

            //clear individuelle Liste und fülle sie mit den mutierten Kanten
            individual.clear();
            individual.addAll(mutatedIndividual);
        }
    }

    private double calculateFitness(List<MyEdge> individual) {
        double totalDistance = getTotalDistance(individual);
        //Je kleiner die Gesamtdistanz, desto besser ist die Fitness
        return 1 / (1 + totalDistance); //Inversion, damit die Fitness umso größer ist, je kleiner die Distanz ist
    }

    //Selektionsverfahren für survival selection
    private List<List<MyEdge>> muPlusLambdaSelection(List<List<MyEdge>> population, List<List<MyEdge>> offspring, int mu) {
        //Kombiniere Eltern- und Kind-Population
        List<List<MyEdge>> combinedPopulation = new ArrayList<>(population);
        combinedPopulation.addAll(offspring);

        //Sortiere die kombinierte Population nach Fitness (in absteigender Reihenfolge)
        combinedPopulation.sort((a, b) -> Double.compare(calculateFitness(b), calculateFitness(a)));

        //Wähle die besten µ Individuen aus der kombinierten Population aus
        List<List<MyEdge>> selectedIndividuals = new ArrayList<>();
        for (int i = 0; i < mu && i < combinedPopulation.size(); i++) {
            selectedIndividuals.add(combinedPopulation.get(i));
        }
        return selectedIndividuals;
    }

    //Repräsentation
    public List<Point> EdgeToPoint(List<MyEdge> route) {
        List<Point> routeNoDistances = new ArrayList<>();
        for (MyEdge myEdge : route) {
            routeNoDistances.add(myEdge.getA());
        }
        return routeNoDistances;
    }

    public List<MyEdge> PointToEdge(List<Point> routeNoDistances) {
        List<MyEdge> route = new ArrayList<>();
        for (int j = 0; j < routeNoDistances.size(); j++) {
            if (j != routeNoDistances.size() - 1) {
                route.add(new MyEdge(routeNoDistances.get(j), routeNoDistances.get(j + 1)));
            } else {
                route.add(new MyEdge(routeNoDistances.get(j), routeNoDistances.get(0)));
            }
        }
        return route;
    }

    //Total Distance
    public int getTotalDistance(List<MyEdge> individual) {
        int totalDistance = 0;
        for (MyEdge partIndividual : individual) {
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

    //MENÜ
    //Setztung der Paramter durch den Nutzer in Form eines Menüs
    void setParameterMenue(Scanner menuScanner) throws IOException {
        String Eingabe;
        System.out.println("\n--- Menü ---\n Bitte geben Sie Ihre Werte ein. Falls Sie das nicht machen werden Standartwerte genutzt.");

        do {
            System.out.println("Einwohnerzahl pro Generation=e\nGenerationen Anzahl = g\nMutationsrate = m\nStadtwechseln = s\nTurniergröße = t\nParameter Ausgeben = p\nEingabe Beenden = Q");
            Eingabe = menuScanner.nextLine();
            switch (Eingabe) {
                case "e"://Einwohnerzahl pro Generation
                case "E":
                    System.out.println("Bitte geben Sie die Einwohneranzahl ein oder Z für zurück");
                    Eingabe = menuScanner.nextLine();
                    if (!Eingabe.equals("z") && !Eingabe.equals("Z")) {
                        this.populationSize = sicherStringZuInt(Eingabe);
                    }
                    break;
                case "g"://Anzahl der Generationen
                case "G":
                    System.out.println("Bitte geben Sie die Generationenanzahl ein oder Z für zurück");
                    Eingabe = menuScanner.nextLine();
                    if (!Eingabe.equals("z") && !Eingabe.equals("Z")) {
                        this.maxGenerations = sicherStringZuInt(Eingabe);
                    }
                    break;/*
                case "k":
                case "K"://Kreuzungsrate
                    System.out.println("Bitte geben Sie die Kreuzungsrate ein oder Z für zurück");
                    Eingabe = menuScanner.nextLine();
                    if (!Eingabe.equals("z") && !Eingabe.equals("Z")) {
                        this.crossoverRate = sicherStringZuDouble(Eingabe);
                    }
                    break;*/
                case "m"://Mutationsrate
                case "M":
                    System.out.println("Bitte geben Sie die Mutationsrate ein oder Z für zurück");
                    Eingabe = menuScanner.nextLine();
                    if (!Eingabe.equals("z") && !Eingabe.equals("Z")) {
                        this.mutationRate = sicherStringZuDouble(Eingabe);
                    }
                    break;
                case "S":
                case "s":
                    berlin=!berlin;
                    if(berlin){
                        this.cities =TSPToGraph.distanceList("src\\berlin52.tsp", 52);
                        System.out.println("Stadt zu Berlin gewechselt");
                    }else {
                        this.cities =TSPToGraph.distanceList("src\\ch150.tsp", 150);
                        System.out.println("Stadt zu Churritz gewechselt");
                    }
                    break;
                case "t"://Turniergröße
                case "T":
                    System.out.println("Bitte geben Sie die Turniergröße ein oder Z für zurück");
                    Eingabe = menuScanner.nextLine();
                    if (!Eingabe.equals("z") && !Eingabe.equals("Z")) {
                        this.tournamentSize = (sicherStringZuInt(Eingabe));
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
                    System.out.println("Eingabe nicht erkannt. Bitte versuchen Sie es erneut.");
            }
        } while (!Eingabe.equals("q") && !Eingabe.equals("Q"));
        System.out.println("Eingabe Beendet.");
    }

    private void alleParameterAusgabe() {
        if(this.berlin) {
            System.out.println("Ihre Stadt ist auf Berlin gestellt.");
        } else {
            System.out.println("Ihre Stadt ist auf Churritz gestellt.");
        }
        System.out.println("Einwohnerzahl: " + this.populationSize);
        //System.out.println("Kreuzungsrate: " + this.crossoverRate);
        System.out.println("Mutationsrate: " + this.mutationRate);
        System.out.println("Turniergröße: " + this.tournamentSize);
        System.out.println("Anzahl der Generationen: " + this.maxGenerations);
    }

    //Menü: Falsche Eingabe des Nutzers abfangen
    private static Integer sicherStringZuInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            System.out.println("Ungültige Eingabe: " + str);
            return null;
        }
    }

    //Menü: Falsche Eingabe des Nutzers abfangen
    private static Double sicherStringZuDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            System.out.println("Ungültige Eingabe: " + str);
            return null;
        }
    }
}