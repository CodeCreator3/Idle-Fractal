package src;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Renderer renderer = new Renderer();
    private double timeSeconds = 5.0;
    private double progress = 0.0;
    public double energy = 30000.0;
    private final double[] BASE_ENERGY = {10.0, 20.0, 40.0}; // Tree, Snowflake, Fern
    private final String ENERGY_UNIT = "Joules";

    public String[] fractalTypes = {"Tree", "Snowflake", "Fern"};
    public double[] unlockThresholds = {0, 5000, 25000}; // Cost to unlock
    public boolean[] fractalUnlocked = {true, false, false};
    public int[] speedLevels = {1, 1, 1};
    public int[] complexityLevels = {1, 1, 1};

    public int unlockedFractals = 1;
    public int currentFractal = 0;
    private double[] progresses = new double[fractalTypes.length];

    public void init() {
        renderer.init(this);
        renderer.refreshFractalsDisplay();
        renderer.refreshUnlockPanel();
        // Optionally reset progresses
        for (int i = 0; i < progresses.length; i++) progresses[i] = 0.0;
    }

    public void run() {
        for (int idx = 0; idx < unlockedFractals; idx++) {
            double timeMillis = timeSeconds * 1000 / speedLevels[idx];
            int delayMillis = 20;
            int frames = (int) (timeMillis / delayMillis);

            progresses[idx] += 1.0 / frames;
            if (progresses[idx] >= 1.0) {
                progresses[idx] = 0.0;
                // Give more energy for higher fractals
                energy += BASE_ENERGY[idx] * (1 + idx) * Math.sqrt(complexityLevels[idx]);
            }
            renderer.fractalPanels[idx].setPercent(progresses[idx]);
        }
        renderer.updateAllFractalPanelEnergy(energy, ENERGY_UNIT);
        renderer.updateEnergyDisplay(energy, ENERGY_UNIT);
    }

    public List<LineSegment> getFractalSegments(int idx) {
        int width = 800, height = 600;
        double startX = width / 2.0;
        double startY = height - 50;
        switch (idx) {
            case 0:
                return FractalGenerator.generateFractalTree(
                    startX, startY, -90, 100, complexityLevels[idx], 25, 0.7);
            case 1:
                return FractalGenerator.generateKochSnowflake(
                    width / 2.0, height / 2.0, 200, complexityLevels[idx]);
            case 2:
                return FractalGenerator.generateFern(
                    width / 2.0, height - 50, complexityLevels[idx]);
            default:
                return new ArrayList<>();
        }
    }

    public boolean buySpeedUpgrade(int idx) {
        double cost = (20.0 * (idx + 1)) * Math.pow(1.8, speedLevels[idx] - 1);
        if (energy >= cost) {
            energy -= cost;
            speedLevels[idx]++;
            return true;
        }
        return false;
    }

    public boolean buyComplexityUpgrade(int idx) {
        double cost = (30.0 * (idx + 1)) * Math.pow(2.0, complexityLevels[idx] - 1);
        if (energy >= cost) {
            energy -= cost;
            complexityLevels[idx]++;
            // Update the fractal's segments after upgrading complexity
            List<LineSegment> newSegments = getFractalSegments(idx);
            renderer.fractalPanels[idx].setSegments(newSegments);
            renderer.fractalPanels[idx].repaint();
            return true;
        }
        return false;
    }

    public boolean unlockFractal(int idx) {
        if (!fractalUnlocked[idx] && energy >= unlockThresholds[idx]) {
            energy -= unlockThresholds[idx];
            fractalUnlocked[idx] = true;
            unlockedFractals++;
            speedLevels[idx] = 1;
            complexityLevels[idx] = 1;
            renderer.refreshFractalsDisplay();
            return true;
        }
        return false;
    }
}