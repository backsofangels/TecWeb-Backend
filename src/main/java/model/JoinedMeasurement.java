/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java.model;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JoinedMeasurement {
    private Date measurementDate;
    private Pollutant pollutantMonitored;
    private double quantityMeasured;

    public JoinedMeasurement() {}

    public JoinedMeasurement (Date measurementDate, Pollutant pollutantMonitored, double quantity) {
        this.measurementDate = measurementDate;
        this.pollutantMonitored = pollutantMonitored;
        this.quantityMeasured = quantity;
    }

    public Date getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(Date measurementDate) {
        this.measurementDate = measurementDate;
    }

    public void setPollutantMonitored(Pollutant pollutantMonitored) {
        this.pollutantMonitored = pollutantMonitored;
    }

    public void setQuantityMeasured(double quantityMeasured) {
        this.quantityMeasured = quantityMeasured;
    }

    public Pollutant getPollutantMonitored() {
        return pollutantMonitored;
    }

    public double getQuantityMeasured() {
        return quantityMeasured;
    }

    public void printDebug () {
        System.out.println(this.measurementDate);
        pollutantMonitored.printPollutant();
        System.out.println(this.quantityMeasured);
    }
}
