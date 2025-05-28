package src.shapes;

public class Circle {
    public double x, y, r, t;

    Circle(double x, double y, double r, double t) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.t = t;
    }

    @Override
    public String toString() {
        return String.format("Circle[(%.2f, %.2f), %.2f, %.2f]", x, y, r, t);
    }
}
