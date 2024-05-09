package supportMethods;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class TSPadvancedVisualizer {
    public void showGraph(List<Point> citys) {
        // Annahme: Die Lösung ist eine Reihenfolge von Städten (Knoten)
        List<Point> solution = citys; // Beispiel-Lösung

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
        VisualizationViewer<Point, String> vv = new VisualizationViewer<>(new CircleLayout<>(graph));
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
