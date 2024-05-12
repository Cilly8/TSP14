package supportMethods;

import javax.swing.*;

import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.visualization.*;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

import java.awt.Dimension;
import java.util.*;

public class ImprovedTSPVisualizer {//Alles eine Methode damit wir leichter beide Lösungen anzeigen können

    public void LoesungsAnzeige(List<Point> solution, String Text) {

        // Erstelle einen Graphen mit den Städten und Kanten entsprechend der Lösung
        Graph<Point, String> graph = new SparseGraph<>();
        for (int i = 0; i < solution.size() - 1; i++) {
            Point currentCity = solution.get(i);
            Point nextCity = solution.get(i + 1);
            graph.addVertex(currentCity);
            graph.addVertex(nextCity);
            graph.addEdge("Edge" + i, currentCity, nextCity);
        }
        Point currentCity = solution.get(solution.size()-1);
        Point nextCity = solution.get(0);
        graph.addVertex(currentCity);
        graph.addVertex(nextCity);
        graph.addEdge("Edge" + solution.size(), currentCity, nextCity);

        // Erstelle den Layout-Algorithmus (Spring Embedder)
        Layout<Point, String> layout = new ISOMLayout<>(graph);

        // Setze die Größe des Layouts
        layout.setSize(new Dimension(1500, 900)); // Größeres Layout

        // Erstelle den Visualisierer mit dem Layout
        VisualizationViewer<Point, String> vv = new VisualizationViewer<>(layout);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
        vv.setPreferredSize(new Dimension(1900, 1000)); // Größeres Fenster

        // Erstelle ein Swing-Fenster und füge den Visualisierer hinzu
        JFrame frame = new JFrame(Text);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);

    }
}