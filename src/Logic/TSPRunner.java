package Logic;

import supportMethods.ImprovedTSPVisualizer;
import supportMethods.MyEdge;
import supportMethods.Point;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class TSPRunner {
    public static void main(String[] args) throws IOException {
        Scanner menuScanner = new Scanner(System.in);
        ImprovedTSPVisualizer myVisualizer = new ImprovedTSPVisualizer();
        List<Point> distancesA = TSPToGraph.distanceList("src\\berlin52.tsp", 52);

        int populationSize = 200;
        double crossoverRate = 1;
        double mutationRate = 0.2;
        int tournamentSize = 5;
        int maxGenerations = 1000;

        GraphAlgo ga = new GraphAlgo(distancesA, populationSize, crossoverRate, mutationRate,
                tournamentSize, maxGenerations);

        menuScanner.close();
        List<MyEdge> solution = ga.solveTSP();//Lösungen erst als Text dann als Graphik ausgeben
        System.out.println("Best solution found: \n" + solution);
        System.out.println("Total distance: " + ga.getTotalDistance(solution));
        myVisualizer.LoesungsAnzeige(ga.EdgeToPoint(solution), "src\\berlin52.tsp Loesung -52 Knoten");
    }
}