/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java.model;

import java.util.Date;

public class Measurement {
    private int measurementID;
    private Date measurementDate;
    private int drillID;
    private int pollutantID;
    private int quantityMeasured; //THE QUANTITY IS ALWAYS EXPRESSED AS DOBULE AND INTERPRETED AS MG/CUBE METER

    public Measurement() {}

    public Measurement(Date measurementDate, int drillID, int pollutantID, int quantityMeasured) {
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

    public int getQuantityMeasured() {
        return quantityMeasured;
    }

    public void setQuantityMeasured(int quantityMeasured) {
        this.quantityMeasured = quantityMeasured;
    }
}
