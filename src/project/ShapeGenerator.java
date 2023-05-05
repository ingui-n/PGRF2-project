package project;

import java.util.ArrayList;
import java.util.Random;

/**
 * algorithm from https://stackoverflow.com/a/25276331/13024461
 */

public class ShapeGenerator {
    double avgRadius, irregularity, spikiness;
    int numVertices;

    public ShapeGenerator(double avgRadius, double irregularity, double spikiness, int numVertices) {
        this.avgRadius = avgRadius;
        this.irregularity = irregularity;
        this.spikiness = spikiness;
        this.numVertices = numVertices;
    }

    public ArrayList<Point> generatePolygon() {
        if (irregularity < 0 || irregularity > 1) {
            throw new IllegalArgumentException("Irregularity must be between 0 and 1.");
        }
        if (spikiness < 0 || spikiness > 1) {
            throw new IllegalArgumentException("Spikiness must be between 0 and 1.");
        }

        irregularity *= 2 * Math.PI / numVertices;
        spikiness *= avgRadius;
        ArrayList<Double> angleSteps = randomAngleSteps(numVertices, irregularity);

        ArrayList<Point> points = new ArrayList<>();
        double angle = Math.random() * 2 * Math.PI;

        for (int i = 0; i < numVertices; i++) {
            double radius = clip(randomGaussian(avgRadius, spikiness), 0, 2 * avgRadius);
            int x = (int) (radius * Math.cos(angle));
            int y = (int) (radius * Math.sin(angle));
            points.add(new Point(x, y));
            angle += angleSteps.get(i);
        }

        return points;
    }

    private ArrayList<Double> randomAngleSteps(int steps, double irregularity) {
        ArrayList<Double> angles = new ArrayList<>();

        double lower = (2 * Math.PI / steps) - irregularity;
        double upper = (2 * Math.PI / steps) + irregularity;
        float cumsum = 0;

        Random random = new Random();

        for (int i = 0; i < steps; i++) {
            double angle = random.nextDouble() * (upper - lower) + lower;
            angles.add(angle);
            cumsum += angle;
        }

        cumsum /= (2 * Math.PI);

        for (int i = 0; i < steps; i++) {
            angles.set(i, angles.get(i) / cumsum);
        }

        return angles;
    }

    private double randomGaussian(double mean, double dev) {
        Random rand = new Random();
        return Math.abs(rand.nextGaussian() * dev + mean);
    }

    private double clip(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }
}
