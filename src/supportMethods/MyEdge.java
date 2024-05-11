package supportMethods;

import supportMethods.Point;

public class MyEdge {
    private Point a;
    private Point b;
    public MyEdge(Point a1, Point b1){
       a=a1;
       b=b1;
    }

    public Point getA() {
        return a;
    }

    public Point getB() {
        return b;
    }

    public double calculateDistance() {
        //Berechnung der Distanz (Länge der Vektoren)
        double xdiff=a.getX()-b.getX();
        double xQuadrat=xdiff*xdiff;
        double ydiff=a.getY()-b.getY();
        double yQuadrat=ydiff*ydiff;
        return Math.sqrt(xQuadrat+yQuadrat);
    }
    @Override
    public String toString(){
        String ausgabe= " Punkt: "+a.toString()+" zu Punkt: "+b.toString()+" hat die Distanz: "+calculateDistance()+"\n";
        return ausgabe;
    }
}
