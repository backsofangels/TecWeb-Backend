/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java.model;

public class Pollutant {
    private int pollutantID;
    private String pollutantName;
    private double maximumThreshold;

    public Pollutant() {}

    public Pollutant(String pollutantName, double maximumThreshold) {
        this.pollutantName = pollutantName;
        this.maximumThreshold = maximumThreshold;
    }

    public void setPollutantID(int pollutantID) {
        this.pollutantID = pollutantID;
    }

    public int getPollutantID() {
        return this.pollutantID;
    }

    public void setPollutantName(String pollutantName) {
        this.pollutantName = pollutantName;
    }

    public String getPollutantName() {
        return this.pollutantName;
    }

    public void setMaximumThreshold(double maximumThreshold) {
        this.maximumThreshold = maximumThreshold;
    }

    public double getMaximumThreshold() {
        return this.maximumThreshold;
    }
}
