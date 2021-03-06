package ee5.demo_color_mixer;

public class Calculator  {
    private double x1,x2,y1,y2;

    public Calculator() {
    }

    public int calcDistance() {
        int distance;
        double xDiff = x2 - x1;
        double yDiff = y2 - y1;
        distance = (int) Math.sqrt(xDiff*xDiff + yDiff*yDiff);
        return distance;
    }

    public double getX1() {
        return x1;
    }

    public double getX2() {
        return x2;
    }

    public double getY1() {
        return y1;
    }

    public double getY2() {
        return y2;
    }

    public void setX1(double a) {
        x1 = a;
    }

    public void setX2(double a) {
        x2 = a;
    }

    public void setY1(double a) {
        y1 = a;
    }

    public void setY2(double a) {
        y2 = a;
    }
}
