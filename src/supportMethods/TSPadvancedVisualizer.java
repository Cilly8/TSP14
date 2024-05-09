package supportMethods;

import Logic.GraphAlgo;
import Logic.TSPToGraph;
import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TSPadvancedVisualizer {
    private TSPToGraph transformer;
    private GraphAlgo computer;
    public static void main(String[] args) throws IOException {
        List<Point> distancesB;
        distancesB = TSPToGraph.distanceList("src\\berlin52.tsp", 52);
        List<Point> distancesA = TSPToGraph.distanceList("src\\gr229.tsp", 229);
        int populationSize = 50;
        double crossoverRate = 0.8;
        double mutationRate = 0.2;
        int tournamentSize = 20;
        int maxGenerations = 1000;

        GraphAlgo ga = new GraphAlgo(distancesB, populationSize, crossoverRate, mutationRate,
                tournamentSize, maxGenerations);
        GraphAlgo ga2 = new GraphAlgo(distancesA, populationSize, crossoverRate, mutationRate,
                tournamentSize, maxGenerations);

        List<MyEdge> solutionEdge = ga.solveTSP();
        // Annahme: Die Lösung ist eine Reihenfolge von Städten (Knoten)
        List<Point> solution = ga.EdgeToPoint(solutionEdge); // Beispiel-Lösung

        // Erstelle einen Graphen mit den Städten und Kanten entsprechend der Lösung
        Graph<Point, String> graph = new SparseGraph<>();
        for (int i = 0; i < solution.size() -1; i++) {
            Point currentCity = solution.get(i);
            Point nextCity = solution.get(i + 1);
            graph.addVertex(currentCity);
            graph.addVertex(nextCity);
            graph.addEdge("Edge" + i, currentCity, nextCity);
        }

        // Erstelle den Visualisierer
        VisualizationViewer<Point, String> vv = new VisualizationViewer<>(new KKLayout<>(graph));
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
        vv.setPreferredSize(new Dimension(1500, 1500));

        // Erstelle ein Swing-Fenster und füge den Visualisierer hinzu
        JFrame frame = new JFrame("TSP Lösung Visualisierung");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }
}
