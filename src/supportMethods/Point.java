package supportMethods;

public class Point {
    private double x;
    private double y;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Point(double x1,double y1){
        x=x1;
        y=y1;
    }

    @Override
    public String toString(){
        String ausgabe= "( "+x+", "+y+" )";
        return ausgabe;
    }
}

