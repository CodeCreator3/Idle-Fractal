package src.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import src.math.ComplexNumber;
import src.shapes.Circle;
import src.shapes.LineSegment;
import src.shapes.Shape;

public class FractalGenerator {
    public static List<Shape> generateFractalTree(
            double x1, double y1, double angle, double length,
            int depth, double angleDelta, double lengthScale) {
        List<Shape> segments = new ArrayList<>();
        generateTreeHelper(x1, y1, angle, length, depth, angleDelta, lengthScale, segments);
        return segments;
    }

    private static void generateTreeHelper(
            double x1, double y1, double angle, double length,
            int depth, double angleDelta, double lengthScale,
            List<Shape> segments) {
        if (depth <= 0 || length < 1)
            return;

        double x2 = x1 + length * Math.cos(Math.toRadians(angle));
        double y2 = y1 + length * Math.sin(Math.toRadians(angle));

        segments.add(new LineSegment(x1, y1, x2, y2));

        generateTreeHelper(x2, y2, angle - angleDelta, length * lengthScale, depth - 1, angleDelta, lengthScale,
                segments);
        generateTreeHelper(x2, y2, angle + angleDelta, length * lengthScale, depth - 1, angleDelta, lengthScale,
                segments);
    }

    public static List<Shape> generateKochSnowflake(double centerX, double centerY, int size, int depth) {
        List<Shape> segments = new ArrayList<>();
        double height = size * Math.sqrt(3) / 2;

        // Calculate the three points of the equilateral triangle
        double x1 = centerX - size / 2.0;
        double y1 = centerY + height / 3.0;
        double x2 = centerX + size / 2.0;
        double y2 = centerY + height / 3.0;
        double x3 = centerX;
        double y3 = centerY - 2 * height / 3.0;

        // Recursively generate each side
        generateKochEdge(x1, y1, x2, y2, depth, segments);
        generateKochEdge(x2, y2, x3, y3, depth, segments);
        generateKochEdge(x3, y3, x1, y1, depth, segments);

        return segments;
    }

    private static void generateKochEdge(double x1, double y1, double x2, double y2, int depth, List<Shape> segments) {
        if (depth == 0) {
            segments.add(new LineSegment(x1, y1, x2, y2));
            return;
        }
        double dx = (x2 - x1) / 3.0;
        double dy = (y2 - y1) / 3.0;

        double xA = x1 + dx;
        double yA = y1 + dy;
        double xB = x1 + 2 * dx;
        double yB = y1 + 2 * dy;

        // Calculate the peak of the equilateral triangle
        double midX = (x1 + x2) / 2.0;
        double midY = (y1 + y2) / 2.0;
        double px = midX + Math.sqrt(3) * (y1 - y2) / 6.0;
        double py = midY + Math.sqrt(3) * (x2 - x1) / 6.0;

        generateKochEdge(x1, y1, xA, yA, depth - 1, segments);
        generateKochEdge(xA, yA, px, py, depth - 1, segments);
        generateKochEdge(px, py, xB, yB, depth - 1, segments);
        generateKochEdge(xB, yB, x2, y2, depth - 1, segments);
    }

    public static List<Shape> generateFern(double centerX, double centerY, int depth) {
        List<Shape> segments = new ArrayList<>();
        generateFernHelper(centerX, centerY, -90, 60, depth, segments);
        return segments;
    }

    private static void generateFernHelper(double x1, double y1, double angle, double length, int depth,
            List<Shape> segments) {
        if (depth <= 0 || length < 2)
            return;

        double x2 = x1 + length * Math.cos(Math.toRadians(angle));
        double y2 = y1 + length * Math.sin(Math.toRadians(angle));
        segments.add(new LineSegment(x1, y1, x2, y2));

        // Main stem
        generateFernHelper(x2, y2, angle, length * 0.87, depth - 1, segments);

        // Left leaflet
        generateFernHelper(x2, y2, angle - 45, length * 0.38, depth - 2, segments);

        // Right leaflet
        generateFernHelper(x2, y2, angle + 45, length * 0.38, depth - 2, segments);
    }

    public static List<Shape> generateSierpinskiTriangle(double centerX, double centerY, int size, int depth) {
        List<Shape> segments = new ArrayList<>();
        generateSierpinskiHelper(centerX, centerY, size, depth, segments);
        return segments;
    }

    private static void generateSierpinskiHelper(double centerX, double centerY, int size, int depth,
            List<Shape> segments) {
        if (depth == 0) {
            double height = size * Math.sqrt(3) / 2;

            // Calculate the three points of the equilateral triangle
            double x1 = centerX - size / 2.0;
            double y1 = centerY + height / 3.0;
            double x2 = centerX + size / 2.0;
            double y2 = centerY + height / 3.0;
            double x3 = centerX;
            double y3 = centerY - 2 * height / 3.0;

            segments.add(new LineSegment(x1, y1, x2, y2));
            segments.add(new LineSegment(x2, y2, x3, y3));
            segments.add(new LineSegment(x3, y3, x1, y1));
        } else {
            generateSierpinskiHelper(centerX - size / 4.0, centerY, (int) (size / 2.0), depth - 1, segments);
            generateSierpinskiHelper(centerX + size / 4.0, centerY, (int) (size / 2.0), depth - 1, segments);
            generateSierpinskiHelper(centerX, centerY - size * Math.sqrt(3) / 4.0, (int) (size / 2.0), depth - 1,
                    segments);
        }
    }

    public static List<Shape> generateApollonianGasket(
            double x, double y, double radius, int depth) {
        List<Circle> circles = new ArrayList<>();
        circles.add(new Circle(x, y, radius)); // Add the initial circle
        double smallerRadius = radius / (2.0 / Math.sqrt(3) + 1.0); // Define the radius for smaller circles
        if (depth > 1) {
            double offset = (radius - smallerRadius) / 2.0;
            circles.add(new Circle(x, y - (radius - smallerRadius), smallerRadius));
            circles.add(new Circle(x + smallerRadius, y + offset, smallerRadius));
            circles.add(new Circle(x - smallerRadius, y + offset, smallerRadius));
            if (depth > 2) {
                generateApollonianHelper(depth, 1, circles);
            }
        }

        List<Shape> segments = new ArrayList<>();
        for (Circle circle : circles) {
            segments.add(circle);
        }
        return segments;
    }

    private static void generateApollonianHelper(int depth, int index, List<Circle> circles) {
        if (depth == 2)
            return;

        // Draw the circle
        circles.add(findNewCircle(circles, new int[] { 0, index, index + 1 }));
        circles.add(findNewCircle(circles, new int[] { 0, index + 1, index + 2 }));
        circles.add(findNewCircle(circles, new int[] { 0, index + 2, index + 3 }));

        generateApollonianHelper(depth - 1, index + 3, circles);
    }

    private static Circle findNewCircle(List<Circle> circles, int[] indices) {
        double[] otherKs = new double[indices.length];
        for (int i = 0; i < indices.length; i++) {
            Circle circle = circles.get(indices[i]);
            otherKs[i] = 1.0 / circle.r;
        }
        otherKs[0] *= -1.0;
        double k = otherKs[0] + otherKs[1] + otherKs[2]
                + 2 * Math.sqrt(otherKs[0] * otherKs[1] + otherKs[1] * otherKs[2] + otherKs[2] * otherKs[0]);
        ComplexNumber[] centers = new ComplexNumber[indices.length];
        for (int i = 0; i < indices.length; i++) {
            centers[i] = circles.get(indices[i]).getCenter();
        }
        ComplexNumber center = 
                centers[0].multiply(otherKs[0])
                .add(centers[1].multiply(otherKs[1]))
                .add(centers[2].multiply(otherKs[2]))
                .add(centers[0].multiply(centers[1]).multiply(otherKs[0]).multiply(otherKs[1])
                        .add(centers[1].multiply(centers[2]).multiply(otherKs[1]).multiply(otherKs[2]))
                        .add(centers[0].multiply(centers[2]).multiply(otherKs[0]).multiply(otherKs[2])).sqrt()
                        .multiply(2))
                .multiply(1.0 / k);
        
        return new Circle(center.a, center.b, 1.0 / k);
    }
}
