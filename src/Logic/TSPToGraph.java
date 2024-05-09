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

    public static List<Point> loadTSP(String filePath, int numLocations) throws IOException {
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
                double x1 = Double.parseDouble(parts[1]);
                //System.out.print("X1: " + x1 + "\n");
                double y1 = Double.parseDouble(parts[2]);
                //System.out.print("Y1: " + y1 + "\n");
               Point a=new Point(x1,y1);
               citys.add(a);
                placeonIndex++;
            } else {
                if (line.startsWith("NODE_COORD_SECTION")) {
                    coordinatesStarted = true;
                }
            }
        }
        reader.close();
        if(citys.size()==numLocations) {
            System.out.println("Es hat geklappt");

        }
        System.out.println("-"+citys.size()+" "+numLocations);
        return citys;
    }

    public static List<Point> distanceList(String filePath, int numLocations) throws IOException {
        List<Point> citys= loadTSP(filePath, numLocations);
        return citys;
        //printMatrix(distanceMatrix);
    }


}
