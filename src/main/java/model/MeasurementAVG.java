/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java.model;

import java.util.Date;
import java.util.Map;

public class MeasurementAVG {
    private Drill drillInformations;
    private Map<Integer, Tuple> measurements;
    private Date minorBoundary;
    private Date maximumBoundary;

    public MeasurementAVG() {}

    public MeasurementAVG (Drill drillInformations, Map<Integer, Tuple> measurements, Date minorBoundary,
                           Date maximumBoundary, double averageMeasurement) {
        this.drillInformations = drillInformations;
        this.measurements = measurements;
        this.minorBoundary = minorBoundary;
        this.maximumBoundary = maximumBoundary;
    }

    public void setDrillInformations(Drill drillInformations) {
        this.drillInformations = drillInformations;
    }

    public void setMeasurements(Map<Integer, Tuple> measurements) {
        this.measurements = measurements;
    }

    public void setMinorBoundary(Date minorBoundary) {
        this.minorBoundary = minorBoundary;
    }

    public void setMaximumBoundary(Date maximumBoundary) {
        this.maximumBoundary = maximumBoundary;
    }

    public Map<Integer, Tuple> getMeasurements() {
        return measurements;
    }

    public Drill getDrillInformations() {
        return drillInformations;
    }


    public Date getMaximumBoundary() {
        return maximumBoundary;
    }

    public Date getMinorBoundary() {
        return minorBoundary;
    }
}
