package supportMethods;

import java.io.IOException;
import java.util.List;

import Logic.GraphAlgo;
import Logic.TSPToGraph;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestGraph {
    private TSPToGraph transformer;
    private GraphAlgo computer;
    @Test
    public void TestAll() throws IOException {
        List<Point> distancesB;
        distancesB = TSPToGraph.distanceList("src\\berlin52.tsp", 52);
        List<Point> distancesA = TSPToGraph.distanceList("src\\ch150.tsp", 150);
        int populationSize = 50;
        double crossoverRate = 0.8;
        double mutationRate = 0.2;
        int tournamentSize = 20;
        int maxGenerations = 1000;

        GraphAlgo ga = new GraphAlgo(distancesB, populationSize, crossoverRate, mutationRate,
                tournamentSize, maxGenerations);
        GraphAlgo ga2 = new GraphAlgo(distancesA, populationSize, crossoverRate, mutationRate,
                tournamentSize, maxGenerations);

        List<MyEdge> solution = ga.solveTSP();
        System.out.println("Best solution found: " + solution);
        System.out.println("Total distance: " + ga.getTotalDistance(solution));

        List<MyEdge> solution2 = ga2.solveTSP();
        System.out.println("Best solution found: " + solution2);
        System.out.println("Total distance: " + ga2.getTotalDistance(solution2));
    }
    @Test
    public void TestReading() throws IOException {
        List<Point> distancesB= TSPToGraph.distanceList("src\\berlin52.tsp", 52);
        int populationSize = 50;
        double crossoverRate = 0.8;
        double mutationRate = 0.2;
        int tournamentSize = 5;
        int maxGenerations = 1000;
        GraphAlgo ga = new GraphAlgo(distancesB, populationSize, crossoverRate, mutationRate,
                tournamentSize, maxGenerations);
        List<MyEdge> distanceA=ga.PointToEdge(distancesB);
        distancesB=ga.EdgeToPoint(distanceA);
       assertEquals(52,distancesB.size());
    }
    @Test
    public void testVisuals() throws IOException {
        //TSPadvancedVisualizer visualizer=new TSPadvancedVisualizer();
        List<Point> distancesB= TSPToGraph.distanceList("src\\berlin52.tsp", 52);
        int populationSize = 50;
        double crossoverRate = 0.8;
        double mutationRate = 0.2;
        int tournamentSize = 5;
        int maxGenerations = 1000;
        GraphAlgo ga = new GraphAlgo(distancesB, populationSize, crossoverRate, mutationRate,
                tournamentSize, maxGenerations);
        List<MyEdge> distanceA=ga.PointToEdge(distancesB);
        distancesB=ga.EdgeToPoint(distanceA);

    }

}
