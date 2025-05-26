package src;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FractalPanel extends JPanel {
    private final List<LineSegment> segments;
    private double percent = 1.0;
    private double energy = 0.0;
    private String unit = "";

    public FractalPanel(List<LineSegment> segments) {
        this.segments = new ArrayList<>(segments);
    }

    public void setPercent(double percent) {
        this.percent = percent;
        repaint();
    }

    public void setEnergy(double energy, String unit) {
        this.energy = energy;
        this.unit = unit;
    }

    public void setSegments(List<LineSegment> segments) {
        this.segments.clear();
        this.segments.addAll(segments);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawFractalTree(g, segments, percent);

//         Draw energy text in the top-left corner
//         g.setColor(Color.WHITE);
//         g.setFont(new Font("Arial", Font.BOLD, 18));
//         g.drawString(formatJoules(energy), 20, 30);
    }

    // Add this helper method to format Joules/kJ
    private String formatJoules(double value) {
        if (value >= 1000) {
            return String.format("%.2f kJ", value / 1000.0);
        } else {
            return String.format("%d Joules", (int)value);
        }
    }

    public static void drawFractalTree(Graphics g, List<LineSegment> segments, double percent) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1.5f));

        // Clamp percent to [0.0, 1.0]
        percent = Math.max(0.0, Math.min(1.0, percent));

        // Step 1: Compute total length of the tree
        double totalLength = 0.0;
        for (LineSegment seg : segments) {
            totalLength += distance(seg);
        }

        // Step 2: Determine how much length to draw
        double targetLength = totalLength * percent;
        double drawnLength = 0.0;

        for (LineSegment seg : segments) {
            double segLength = distance(seg);
            if (drawnLength + segLength <= targetLength) {
                // Draw full segment
                g2d.drawLine((int) seg.x1, (int) seg.y1, (int) seg.x2, (int) seg.y2);
                drawnLength += segLength;
            } else {
                // Draw partial segment
                double remaining = targetLength - drawnLength;
                double t = remaining / segLength;

                double x2 = seg.x1 + t * (seg.x2 - seg.x1);
                double y2 = seg.y1 + t * (seg.y2 - seg.y1);

                g2d.drawLine((int) seg.x1, (int) seg.y1, (int) x2, (int) y2);
                break;
            }
        }
    }

    // Helper function to compute length of a segment
    private static double distance(LineSegment seg) {
        double dx = seg.x2 - seg.x1;
        double dy = seg.y2 - seg.y1;
        return Math.sqrt(dx * dx + dy * dy);
    }
}