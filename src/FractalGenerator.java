package src;
import java.util.ArrayList;
import java.util.List;

public class FractalGenerator {
    public static List<LineSegment> generateFractalTree(
            double x1, double y1, double angle, double length,
            int depth, double angleDelta, double lengthScale
    ) {
        List<LineSegment> segments = new ArrayList<>();
        generateTreeHelper(x1, y1, angle, length, depth, angleDelta, lengthScale, segments);
        return segments;
    }

    private static void generateTreeHelper(
            double x1, double y1, double angle, double length,
            int depth, double angleDelta, double lengthScale,
            List<LineSegment> segments
    ) {
        if (depth <= 0 || length < 1) return;

        double x2 = x1 + length * Math.cos(Math.toRadians(angle));
        double y2 = y1 + length * Math.sin(Math.toRadians(angle));

        segments.add(new LineSegment(x1, y1, x2, y2));

        generateTreeHelper(x2, y2, angle - angleDelta, length * lengthScale, depth - 1, angleDelta, lengthScale, segments);
        generateTreeHelper(x2, y2, angle + angleDelta, length * lengthScale, depth - 1, angleDelta, lengthScale, segments);
    }

    public static List<LineSegment> generateKochSnowflake(double centerX, double centerY, int size, int depth) {
        List<LineSegment> segments = new ArrayList<>();
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

    private static void generateKochEdge(double x1, double y1, double x2, double y2, int depth, List<LineSegment> segments) {
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

    public static List<LineSegment> generateFern(double centerX, double centerY, int depth) {
        List<LineSegment> segments = new ArrayList<>();
        generateFernHelper(centerX, centerY, -90, 60, depth, segments);
        return segments;
    }

    private static void generateFernHelper(double x1, double y1, double angle, double length, int depth, List<LineSegment> segments) {
        if (depth <= 0 || length < 2) return;

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

    public static List<LineSegment> generateSierpinskiTriangle(double centerX, double centerY, int size, int depth) {
        List<LineSegment> segments = new ArrayList<>();
        generateSierpinskiHelper(centerX, centerY, size, depth, segments);
        return segments;
    }

    private static void generateSierpinskiHelper(double centerX, double centerY, int size, int depth, List<LineSegment> segments) {
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
            generateSierpinskiHelper(centerX, centerY - size * Math.sqrt(3) / 4.0, (int) (size / 2.0), depth - 1, segments);
        }
    }
}
