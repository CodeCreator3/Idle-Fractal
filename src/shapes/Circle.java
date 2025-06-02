package src.shapes;

import java.util.Vector;

import src.math.ComplexNumber;

public class Circle extends Shape {
    public double x, y, r, t;

    public Circle(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public ComplexNumber getCenter() {
        return new ComplexNumber(x, y);
    }

    @Override
    public String toString() {
        return String.format("Circle[(%.2f, %.2f), %.2f]", x, y, r);
    }
}
