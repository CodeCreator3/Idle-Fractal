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
}
