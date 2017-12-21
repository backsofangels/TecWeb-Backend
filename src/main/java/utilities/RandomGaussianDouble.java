/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java.utilities;

import java.util.Random;

public class RandomGaussianDouble {
    private static RandomGaussianDouble instance = null;
    private Random randomGenerator = new Random();
    private double mean = 4.0f;
    private double variance = 2.0f;

    private RandomGaussianDouble() {}

    public static RandomGaussianDouble getInstance() {
        if (instance == null) {
            instance = new RandomGaussianDouble();
        }
        return instance;
    }

    public double getGaussian() {
        return mean + randomGenerator.nextGaussian() * variance;
    }
}
