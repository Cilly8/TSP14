package Logic;

import supportMethods.MyEdge;
import supportMethods.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TSPToGraph {
    private static List<Point> citys = new ArrayList<>();
    private List<MyEdge> distances = new ArrayList<>();

    public static List<Point> loadTSP(String filePath, int numLocations) throws IOException {
        double[][] distanceMatrix = new double[numLocations][numLocations];
        // Read the TSP file
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        boolean coordinatesStarted = false;
        int placeonIndex = 0;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("EOF")) {
                break;
            }
            if (coordinatesStarted) {
                String[] parts = line.trim().split("\\s+");
                double x1 = Double.parseDouble(parts[1]);
                double y1 = Double.parseDouble(parts[2]);
                Point a = new Point(x1, y1);
                citys.add(a);
                int otherplaceonIndex = placeonIndex + 1; // Beginne mit dem nächsten Ort
                placeonIndex++;
            } else {
                if (line.startsWith("NODE_COORD_SECTION")) {
                    coordinatesStarted = true;
                }
            }
        }
        reader.close();
        return citys;
    }

    public static List<Point> distanceList(String filePath, int numLocations) throws IOException {
        List<Point> citys = loadTSP(filePath, numLocations);
        return citys;
    }
}
