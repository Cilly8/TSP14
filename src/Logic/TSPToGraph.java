package Logic;

import supportMethods.MyEdge;
import supportMethods.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TSPToGraph {
    private static List<Point> citys=new ArrayList<>();
     private  List<MyEdge> distances=new ArrayList<>();
    public static List<Point> loadTSP(String filePath, int numLocations) throws IOException {
        double[][] distanceMatrix = new double[numLocations][numLocations];
        //System.out.print("Distance Matrix: " + distanceMatrix + "\n");

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
                //System.out.print("Parts: " + parts[0] + "\n");
                //System.out.print("Parts: " + parts[1] + "\n");
                //System.out.print("Parts: " + parts[2] + "\n");
                double x1 = Double.parseDouble(parts[1]);
                //System.out.print("X1: " + x1 + "\n");
                double y1 = Double.parseDouble(parts[2]);
                //System.out.print("Y1: " + y1 + "\n");
               Point a=new Point(x1,y1);
               citys.add(a);
                int otherplaceonIndex = placeonIndex + 1; // Beginne mit dem nächsten Ort
                //System.out.print("PlaceonIndex: " + placeonIndex + "\n");
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


    public void printDistances(List<MyEdge> distances) {
        for (MyEdge distance : distances) {
            System.out.println(distance.toString());
        }
    }
    public static List<Point> distanceList(String filePath, int numLocations) throws IOException {
        List<Point> citys= loadTSP(filePath, numLocations);
        return citys;
        //printMatrix(distanceMatrix);
    }


}
