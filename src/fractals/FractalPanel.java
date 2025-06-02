package src.fractals;

import javax.swing.*;

import src.shapes.Circle;
import src.shapes.LineSegment;
import src.shapes.Shape;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FractalPanel extends JPanel {
    private final List<Shape> shapes;
    private double percent = 1.0;
    private double energy = 0.0;
    private String unit = "";
    private double xOffset, yOffset;

    public FractalPanel(List<Shape> shapes, double xOffset, double yOffset) {
        this.shapes = new ArrayList<>(shapes);
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public void setPercent(double percent) {
        this.percent = percent;
        repaint();
    }

    public void setEnergy(double energy, String unit) {
        this.energy = energy;
        this.unit = unit;
    }

    public void setShapes(List<Shape> shapes) {
        this.shapes.clear();
        this.shapes.addAll(shapes);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(shapes.get(0) instanceof Circle) {
            List<Circle> circles = new ArrayList<>();
            for (Shape shape : shapes) {
                if (shape instanceof Circle circle) {
                    circles.add(circle);
                }
            }
            drawFractalCircles(g, circles, percent, xOffset, yOffset);
        } else {
            List<LineSegment> segments = new ArrayList<>();
            for (Shape shape : shapes) {
                if (shape instanceof LineSegment lineSegment) {
                    segments.add(lineSegment);
                }
            }
            drawFractalSegments(g, segments, percent, xOffset, yOffset);
        }
    }

    // Add this helper method to format Joules/kJ
    private String formatJoules(double value) {
        if (value >= 1000) {
            return String.format("%.2f kJ", value / 1000.0);
        } else {
            return String.format("%d Joules", (int)value);
        }
    }

    public static void drawFractalSegments(Graphics g, List<LineSegment> segments, double percent, double xOffset, double yOffset) {
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
            LineSegment newSeg = new LineSegment(seg.x1 + xOffset, seg.y1 + yOffset, seg.x2 + xOffset, seg.y2 + yOffset);
            if (drawnLength + segLength <= targetLength) {
                // Draw full segment
                g2d.drawLine((int) newSeg.x1, (int) newSeg.y1, (int) newSeg.x2, (int) newSeg.y2);
                drawnLength += segLength;
            } else {
                // Draw partial segment
                double remaining = targetLength - drawnLength;
                double t = remaining / segLength;

                double x2 = newSeg.x1 + t * (newSeg.x2 - newSeg.x1);
                double y2 = newSeg.y1 + t * (newSeg.y2 - newSeg.y1);

                g2d.drawLine((int) newSeg.x1, (int) newSeg.y1, (int) x2, (int) y2);
                break;
            }
        }
    }

    public static void drawFractalCircles(Graphics g, List<Circle> circles, double percent, double xOffset, double yOffset) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1.5f));

        // Clamp percent to [0.0, 1.0]
        percent = Math.max(0.0, Math.min(1.0, percent));

        // Step 1: Compute total length of the tree
        double totalLength = 0.0;
        for (Circle circle : circles) {
            totalLength += distance(circle);
        }

        // Step 2: Determine how much length to draw
        double targetLength = totalLength * percent;
        double drawnLength = 0.0;

        for (Circle circle : circles) {
            double circleLength = distance(circle);
            Circle newCircle = new Circle(circle.x + xOffset, circle.y + yOffset, circle.r);
            if (drawnLength + circleLength <= targetLength) {
                // Draw full segment
                g2d.drawArc((int)(newCircle.x - newCircle.r), (int)(newCircle.y - newCircle.r), (int)(newCircle.r * 2), (int)(newCircle.r * 2), 0,  360);
                drawnLength += circleLength;
            } else {
                // Draw partial segment
                double remaining = targetLength - drawnLength;
                double t = remaining / circleLength;

                g2d.drawArc((int)(newCircle.x - newCircle.r), (int)(newCircle.y - newCircle.r), (int)(newCircle.r * 2), (int)(newCircle.r * 2), 0, (int) (360 * t));
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

    private static double distance(Circle circle) {
        return 2 * Math.PI * circle.r; // Circumference of the circle
    }
}