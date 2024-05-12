package Logic;

import supportMethods.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TSPToGraph {
    private static List<Point> cities = new ArrayList<>();

    public static List<Point> loadTSP(String filePath) throws IOException {
        // Read the TSP file
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        cities.clear();
        boolean coordinatesStarted = false;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("EOF")) {
                break;
            }
            if (coordinatesStarted) {
                String[] parts = line.trim().split("\\s+");
                double x1 = Double.parseDouble(parts[1]);
                double y1 = Double.parseDouble(parts[2]);
                Point a = new Point(x1, y1);
                cities.add(a);
            } else {
                if (line.startsWith("NODE_COORD_SECTION")) {
                    coordinatesStarted = true;
                }
            }
        }
        reader.close();
        return cities;
    }

    public static List<Point> distanceList(String filePath, int numLocations) throws IOException {
        List<Point> cities = loadTSP(filePath);
        return cities;
    }
}