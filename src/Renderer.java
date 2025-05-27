package src;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

public class Renderer {
    private JFrame frame = new JFrame("Idle Fractal");
    private JPanel containerPanel = new JPanel();
    private JPanel fractalsPanel; // Holds all unlocked fractals (left)
    private JPanel unlockPanel;   // Holds locked fractals (right)
    public FractalPanel[] fractalPanels;
    private Game game;
    private JLabel energyLabel;

    public void init(Game game) {
        this.game = game;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 900);

        containerPanel.setLayout(new BorderLayout());
        containerPanel.setBackground(Color.BLACK);

        JLabel label = new JLabel("Idle Fractal");
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        containerPanel.add(label, BorderLayout.NORTH);

        energyLabel = new JLabel();
        energyLabel.setForeground(Color.WHITE);
        energyLabel.setFont(new Font("Arial", Font.BOLD, 22));
        energyLabel.setHorizontalAlignment(SwingConstants.LEFT);
        energyLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        containerPanel.add(energyLabel, BorderLayout.NORTH);

        fractalsPanel = new JPanel();
        fractalsPanel.setLayout(new BoxLayout(fractalsPanel, BoxLayout.X_AXIS)); // Horizontal layout
        fractalsPanel.setBackground(Color.BLACK);

        unlockPanel = new JPanel();
        unlockPanel.setLayout(new BoxLayout(unlockPanel, BoxLayout.Y_AXIS));
        unlockPanel.setBackground(new Color(30, 30, 40));
        unlockPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 200, 255), 2, true),
            "Unlock Fractals",
            0, 0,
            new Font("Arial", Font.BOLD, 22),
            new Color(100, 200, 255)
        ));

        containerPanel.add(fractalsPanel, BorderLayout.CENTER);
        containerPanel.add(unlockPanel, BorderLayout.EAST);

        frame.add(containerPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        refreshFractalsDisplay();
        refreshUnlockPanel();
        updateEnergyDisplay(game.energy, "Joules");
    }

    // Call this whenever fractals are unlocked or upgraded
    public void refreshFractalsDisplay() {
        fractalsPanel.removeAll();
        int n = game.unlockedFractals;
        if (fractalPanels == null || fractalPanels.length != n) {
            fractalPanels = new FractalPanel[n];
        }
        for (int i = 0; i < n; i++) {
            final int idx = i;
            JPanel singleFractalPanel = new JPanel();
            singleFractalPanel.setLayout(new BorderLayout());
            singleFractalPanel.setBackground(Color.BLACK);

            // Fractal drawing
            List<LineSegment> segments = game.getFractalSegments(idx);
            FractalPanel panel = new FractalPanel(segments, singleFractalPanel.getX()+(675.0/n)+10*(n-1), 550.0);
            panel.setBackground(Color.BLACK);
            panel.setPreferredSize(new Dimension(400, 400));
            panel.setEnergy(game.energy, "Joules");
            fractalPanels[idx] = panel;
            singleFractalPanel.add(panel, BorderLayout.CENTER);

            // Upgrades
            JPanel upgrades = new JPanel();
            upgrades.setLayout(new BoxLayout(upgrades, BoxLayout.Y_AXIS));
            upgrades.setBackground(Color.BLACK);

            JButton speedBtn = new JButton("Upgrade Speed");
            speedBtn.setFont(new Font("Arial", Font.BOLD, 16));
            speedBtn.setBackground(new Color(60, 120, 200));
            speedBtn.setForeground(Color.WHITE);
            speedBtn.setFocusPainted(false);
            speedBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            speedBtn.setToolTipText("Increase speed for this fractal!");
            JLabel speedInfo = new JLabel(getSpeedInfo(game, idx));
            speedInfo.setForeground(Color.WHITE);
            speedInfo.setFont(new Font("Arial", Font.PLAIN, 14));
            speedInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

            speedBtn.addActionListener(e -> {
                if (game.buySpeedUpgrade(idx)) {
                    speedInfo.setText(getSpeedInfo(game, idx));
                }
            });

            JButton complexityBtn = new JButton("Upgrade Complexity");
            complexityBtn.setFont(new Font("Arial", Font.BOLD, 16));
            complexityBtn.setBackground(new Color(60, 200, 120));
            complexityBtn.setForeground(Color.WHITE);
            complexityBtn.setFocusPainted(false);
            complexityBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            complexityBtn.setToolTipText("Increase complexity for this fractal!");
            JLabel complexityInfo = new JLabel(getComplexityInfo(game, idx));
            complexityInfo.setForeground(Color.WHITE);
            complexityInfo.setFont(new Font("Arial", Font.PLAIN, 14));
            complexityInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

            complexityBtn.addActionListener(e -> {
                if (game.buyComplexityUpgrade(idx)) {
                    complexityInfo.setText(getComplexityInfo(game, idx));
                    // Optionally update the fractal drawing
                }
            });

            upgrades.add(speedBtn);
            upgrades.add(speedInfo);
            upgrades.add(Box.createVerticalStrut(8));
            upgrades.add(complexityBtn);
            upgrades.add(complexityInfo);

            singleFractalPanel.add(upgrades, BorderLayout.SOUTH);

            // Label for fractal type
            JLabel fractalLabel = new JLabel(game.fractalTypes[idx]);
            fractalLabel.setForeground(Color.WHITE);
            fractalLabel.setFont(new Font("Arial", Font.BOLD, 20));
            fractalLabel.setHorizontalAlignment(SwingConstants.CENTER);
            singleFractalPanel.add(fractalLabel, BorderLayout.NORTH);

            fractalsPanel.add(singleFractalPanel);
            fractalsPanel.add(Box.createVerticalStrut(20));
        }
        fractalsPanel.revalidate();
        fractalsPanel.repaint();
    }

    // Call this whenever energy changes or a fractal is unlocked
    public void refreshUnlockPanel() {
        unlockPanel.removeAll();
        for (int i = 0; i < game.fractalTypes.length; i++) {
            if (!game.fractalUnlocked[i]) {
                final int idx = i;
                JPanel lockPanel = new JPanel();
                lockPanel.setLayout(new BoxLayout(lockPanel, BoxLayout.Y_AXIS));
                lockPanel.setBackground(new Color(30, 30, 40));
                JLabel label = new JLabel("Unlock " + game.fractalTypes[i]);
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Arial", Font.BOLD, 18));
                label.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel costLabel = new JLabel("Cost: " + formatJoules(game.unlockThresholds[i]));
                costLabel.setForeground(Color.WHITE);
                costLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                costLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JButton unlockBtn = new JButton("Unlock");
                unlockBtn.setFont(new Font("Arial", Font.BOLD, 16));
                unlockBtn.setBackground(new Color(200, 120, 60));
                unlockBtn.setForeground(Color.WHITE);
                unlockBtn.setFocusPainted(false);
                unlockBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

                unlockBtn.setEnabled(game.energy >= game.unlockThresholds[i]);
                unlockBtn.addActionListener(e -> {
                    if (game.unlockFractal(idx)) {
                        refreshUnlockPanel();
                        refreshFractalsDisplay();
                    }
                });

                lockPanel.add(label);
                lockPanel.add(costLabel);
                lockPanel.add(unlockBtn);
                unlockPanel.add(lockPanel);
                unlockPanel.add(Box.createVerticalStrut(20));
            }
        }
        unlockPanel.revalidate();
        unlockPanel.repaint();
    }

    // Helper methods for displaying info
    private String formatJoules(double value) {
        if (value >= 1000000) {
            return String.format("%.2f mJ", value / 1000000.0);
        } 
        if (value >= 1000) {
            return String.format("%.2f kJ", value / 1000.0);
        } 
        return String.format("%d Joules", (int)value);
    }

    private String getSpeedInfo(Game game, int idx) {
        double cost = game.getSpeedCost(idx);
        return "Level: " + game.speedLevels[idx] + "   Cost: " + formatJoules(cost);
    }

    private String getComplexityInfo(Game game, int idx) {
        double cost = game.getComplexityCost(idx);
        return "Level: " + game.complexityLevels[idx] + "   Cost: " + formatJoules(cost);
    }

    public void updateAllFractalPanelEnergy(double energy, String unit) {
        if (fractalPanels != null) {
            for (int i = 0; i < fractalPanels.length; i++) {
                if (fractalPanels[i] != null) {
                    fractalPanels[i].setEnergy(energy, unit);
                    fractalPanels[i].repaint();
                }
            }
        }
    }

    public void updateEnergyDisplay(double energy, String unit) {
        energyLabel.setText("Energy: " + formatJoules(energy));
    }
}
