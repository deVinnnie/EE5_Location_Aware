package ee5.demo_color_mixer;

/**
 * Created by Matthias on 7/05/2015.
 */
public enum BackgroundColor {
    YELLOW(255,255,255,0), BLUE(255,0,0,255), RED(255,255,0,0),
    GREEN(255,0,255,0), ORANGE(255,255,127,0), PURPLE(255,255,0,255),
    WHITE(255,255,255,255);

    private int a;
    private int r;
    private int g;
    private int b;

    private BackgroundColor(int a, int r, int g, int b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public void setA(int a) {
        this.a = a;
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setG(int g) {
        this.g = g;
    }

    public void setB(int b) {
        this.b = b;
    }

}
