package src;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

public class Renderer {
    private JFrame frame = new JFrame("Idle Fractal");
    private JPanel containerPanel = new JPanel(); // Use a layout manager panel
    private FractalPanel fractalPanel;

    public void init() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 900);

        containerPanel.setLayout(new BorderLayout());
        containerPanel.setBackground(Color.BLACK);

        JLabel label = new JLabel("Idle Fractal");
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        containerPanel.add(label, BorderLayout.NORTH);

        frame.add(containerPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void render(List<LineSegment> fractal) {
        if (fractalPanel != null) {
            containerPanel.remove(fractalPanel);
        }

        fractalPanel = new FractalPanel(fractal);
        fractalPanel.setBackground(Color.BLACK); // match background
        containerPanel.add(fractalPanel, BorderLayout.CENTER);
        containerPanel.revalidate();
        containerPanel.repaint();

        // Start the animation
        new Timer(20, new ActionListener() {
            double progress = 0.0;

            public void actionPerformed(ActionEvent e) {
                progress += 0.01;
                if (progress >= 1.0) {
                    progress = 1.0;
                    ((Timer) e.getSource()).stop();
                }
                fractalPanel.setPercent(progress);
            }
        }).start();
    }
}
