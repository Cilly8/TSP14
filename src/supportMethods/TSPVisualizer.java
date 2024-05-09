package supportMethods;

import javax.swing.*;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.visualization.*;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.Dimension;
import java.util.*;

public class TSPVisualizer {

    public static void main(String[] args) {
        // Annahme: Die Lösung ist eine Reihenfolge von Städten (Knoten)
        List<Integer> solution = Arrays.asList(1, 2, 3, 4, 1); // Beispiel-Lösung

        // Erstelle einen Graphen mit den Städten und Kanten entsprechend der Lösung
        Graph<Integer, String> graph = new SparseGraph<>();
        for (int i = 0; i < solution.size() - 1; i++) {
            int currentCity = solution.get(i);
            int nextCity = solution.get(i + 1);
            graph.addVertex(currentCity);
            graph.addVertex(nextCity);
            graph.addEdge("Edge" + i, currentCity, nextCity);
        }

        // Erstelle den Visualisierer
        VisualizationViewer<Integer, String> vv = new VisualizationViewer<>(new CircleLayout<>(graph));
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
        vv.setPreferredSize(new Dimension(400, 300));

        // Erstelle ein Swing-Fenster und füge den Visualisierer hinzu
        JFrame frame = new JFrame("TSP Lösung Visualisierung");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }
}