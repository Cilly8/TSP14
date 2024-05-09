package Cilly;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Math;

public class TSPLoader {

    public static double[][] loadTSP(String filePath, int numLocations) throws IOException {
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
                int otherplaceonIndex = placeonIndex + 1; // Beginne mit dem nächsten Ort
                //System.out.print("PlaceonIndex: " + placeonIndex + "\n");
                while (otherplaceonIndex < numLocations) {
                    String[] otherParts = reader.readLine().trim().split("\\s+"); //Neuer Reader für anderen Ort benötigt
                    double x2 = Double.parseDouble(otherParts[1]);
                    //System.out.print("X2: " + x2 + "\n");
                    double y2 = Double.parseDouble(otherParts[2]);
                    //System.out.print("Y2: " + y2 + "\n");
                    double distance = calculateDistance(x1, y1, x2, y2);
                    //System.out.print("Distance: " + distance + "\n");
                    distanceMatrix[placeonIndex][otherplaceonIndex] = distance;
                    distanceMatrix[otherplaceonIndex][placeonIndex] = distance;
                    //System.out.print("Distance Matrix: " + distanceMatrix + "\n");
                    otherplaceonIndex++;
                }
                placeonIndex++;
            } else {
                if (line.startsWith("NODE_COORD_SECTION")) {
                    coordinatesStarted = true;
                }
            }
        }
        reader.close();
        return distanceMatrix;
    }

    public static double calculateDistance(double x1, double y1, double x2, double y2) {
        //Berechnung der Distanz (Länge der Vektoren)
        //System.out.print(Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)) + "\n");
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            for (double val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }

    public static double[][] distanceMatrix(String filePath, int numLocations) throws IOException {
        double[][] distanceMatrix = loadTSP(filePath, numLocations);
        return distanceMatrix;
        //printMatrix(distanceMatrix);
    }
}



