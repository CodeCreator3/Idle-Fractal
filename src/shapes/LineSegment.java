package src.shapes;


public class LineSegment extends Shape {
    public double x1, y1, x2, y2;

    public LineSegment(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public String toString() {
        return String.format("LineSegment[(%.2f, %.2f), (%.2f, %.2f)]", x1, y1, x2, y2);
    }
}