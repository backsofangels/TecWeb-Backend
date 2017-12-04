/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java.model;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.Date;

public class Measurement {
    private int measurementID;
    private Date measurementDate;
    private int drillID;
    private int pollutantID;
    private double quantityMeasured; //THE QUANTITY IS ALWAYS EXPRESSED AS DOBULE AND INTERPRETED AS MG/CUBE METER

    public Measurement() {}

    public Measurement(Date measurementDate, int drillID, int pollutantID, double quantityMeasured) {
        this.measurementDate = measurementDate;
        this.drillID = drillID;
        this.pollutantID = pollutantID;
        this.quantityMeasured = quantityMeasured;
    }
    public int getMeasurementID() {
        return measurementID;
    }

    public void setMeasurementID(int measurementID) {
        this.measurementID = measurementID;
    }

    public Date getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(Date measurementDate) {
        this.measurementDate = measurementDate;
    }

    public int getDrillID() {
        return drillID;
    }

    public void setDrillID(int drillID) {
        this.drillID = drillID;
    }

    public int getPollutantID() {
        return pollutantID;
    }

    public void setPollutantID(int pollutantID) {
        this.pollutantID = pollutantID;
    }

    public double getQuantityMeasured() {
        return quantityMeasured;
    }

    public void setQuantityMeasured(double quantityMeasured) {
        this.quantityMeasured = quantityMeasured;
    }

    public void printMeasurement() {
        System.out.println("meas id " + this.measurementID);
        System.out.println("meas date" + this.measurementDate);
        System.out.println("meas drill " + this.drillID);
        System.out.println("poll id" + this.pollutantID);
        System.out.println("quantity " + this.quantityMeasured);
    }
}
