package com.github.sashasorokin.awareofvillagers;

import org.bukkit.Location;

import java.util.Random;

public class CoordinatesObfuscator {
    private final Random random = new Random(System.currentTimeMillis());
    private double minRange;
    private double maxRange;

    public CoordinatesObfuscator() {
        setMinRange(0);
        setMaxRange(0);
    }

    public CoordinatesObfuscator(double minRange, double maxRange) {
        setMinRange(minRange);
        setMaxRange(maxRange);
    }

    @SuppressWarnings("unused")
    public double getMinRange() {
        return minRange;
    }

    public void setMinRange(double minRange) {
        this.minRange = minRange;
    }

    @SuppressWarnings("unused")
    public double getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(double maxRange) {
        this.maxRange = maxRange;
    }

    private double getRandomDouble() {
        boolean invert = random.nextBoolean();

        double nextDouble = minRange + (random.nextDouble() * (maxRange - minRange));

        return invert ? -nextDouble : nextDouble;
    }

    public Location obfuscate(Location location) {
        return location.add(getRandomDouble(), 0, getRandomDouble());
    }
}
