import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TSPLoader {

    public static double[][] loadTSP(String filePath, int numCities) throws IOException {
        double[][] distanceMatrix = new double[numCities][numCities];

        // Read the TSP file
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        boolean coordinatesStarted = false;
        int cityIndex = 0;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("EOF")) {
                break;
            }
            if (coordinatesStarted) {
                String[] parts = line.trim().split("\\s+");
                System.out.println(parts[0]);
                System.out.println(parts[1]);
                System.out.println(parts[2]);
                double x1 = Double.parseDouble(parts[1]);
                double y1 = Double.parseDouble(parts[2]);
                int otherCityIndex = cityIndex + 1; // Beginne mit der nächsten Stadt
                while (otherCityIndex < numCities) {
                    double x2 = Double.parseDouble(parts[1]);
                    double y2 = Double.parseDouble(parts[2]);
                    double distance = calculateDistance(x1, y1, x2, y2);
                    distanceMatrix[cityIndex][otherCityIndex] = distance;
                    distanceMatrix[otherCityIndex][cityIndex] = distance;
                    otherCityIndex++;
                }
                cityIndex++;
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
        // Euklidische Formel zur Berechnung der Distanz (Länge der Vektoren)
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static void main(String[] args) throws IOException {
        String filePath = "src/berlin52.tsp"; // Pfad zur TSP-Datei
        int numCities = 52; // Anzahl der Städte in der TSP-Datei
        double[][] distanceMatrix = loadTSP(filePath, numCities);

        // Entfernungsmatrix ausgeben
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                System.out.print(distanceMatrix[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
