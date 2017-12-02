/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java.model;

public class Drill {
    private int drillID;
    private double xCoordinate;
    private double yCoordinate;

    public Drill() {}

    public Drill(Double xCoordinate, Double yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public void setdrillID(int drillID) {
        this.drillID = drillID;
    }

    public int getdrillID() {
        return this.drillID;
    }

    public void setxCoordinate(Double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public double getxCoordinate() {
        return this.xCoordinate;
    }

    public void setyCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public double getyCoordinate() {
        return this.yCoordinate;
    }
}
