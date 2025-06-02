package src;


import java.util.ArrayList;
import java.util.List;

import src.fractals.FractalGenerator;
import src.shapes.LineSegment;
import src.shapes.Shape;

public class Game {
    private Renderer renderer = new Renderer();
    private double timeSeconds = 5.0;
    public double energy = 0.0;
    private final double[] BASE_ENERGY = {10.0, 20.0, 40.0, 80.0, 100.0}; // Tree, Snowflake, Fern
    private final String ENERGY_UNIT = "Joules";

    public String[] fractalTypes = {"Tree", "Snowflake", "Fern", "Triangle", "Circle"};
    public double[] unlockThresholds = {0, 5000, 25000, 5000000, 50000000}; // Cost to unlock
    public boolean[] fractalUnlocked = {true, false, false, false, false};
    public int[] speedLevels = {1, 1, 1, 1, 1};
    public int[] complexityLevels = {1, 1, 1, 1, 1};

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
                energy += BASE_ENERGY[idx] * (Math.pow(1+ idx, 4)) * complexityLevels[idx];
            }
            renderer.fractalPanels[idx].setPercent(progresses[idx]);
        }
        renderer.updateAllFractalPanelEnergy(energy, ENERGY_UNIT);
        renderer.updateEnergyDisplay(energy, ENERGY_UNIT);
        renderer.refreshUnlockPanel();
    }

    public List<Shape> getFractalShapes(int idx) {
        switch (idx) {
            case 0:
                return FractalGenerator.generateFractalTree(
                    0, 0, -90, 100, complexityLevels[idx], 25, 0.7);
            case 1:
                return FractalGenerator.generateKochSnowflake(
                    0, -200, 200, complexityLevels[idx]);
            case 2:
                return FractalGenerator.generateFern(
                    0, 0, complexityLevels[idx]);
            case 3:
                return FractalGenerator.generateSierpinskiTriangle(
                    0, -200, 200, complexityLevels[idx]);
            case 4:
                return FractalGenerator.generateApollonianGasket(
                    0, -200, 130, complexityLevels[idx]);
            default:
                return new ArrayList<>();
        }
    }

    public boolean buySpeedUpgrade(int idx) {
        double cost = getSpeedCost(idx);
        if (energy >= cost) {
            energy -= cost;
            speedLevels[idx]++;
            return true;
        }
        return false;
    }

    public double getSpeedCost(int idx) {
        return (20.0 * (Math.pow(idx + 1, 5))) * Math.pow(1.8, speedLevels[idx] - 1);
    }

    public boolean buyComplexityUpgrade(int idx) {
        double cost = getComplexityCost(idx);
        if (energy >= cost) {
            energy -= cost;
            complexityLevels[idx]++;
            // Update the fractal's segments after upgrading complexity
            List<Shape> newSegments = getFractalShapes(idx);
            renderer.fractalPanels[idx].setShapes(newSegments);
            renderer.fractalPanels[idx].repaint();
            return true;
        }
        return false;
    }

    public double getComplexityCost(int idx) {
        return (30.0 * (Math.pow(idx + 1, 5))) * Math.pow(2.0, complexityLevels[idx] - 1);
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