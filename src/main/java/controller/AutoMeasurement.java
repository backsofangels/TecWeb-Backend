/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java.controller;
import main.java.model.DrillDAO;
import main.java.model.MeasurementDAO;
import main.java.model.PollutantDAO;
import main.java.utilities.RandomGaussianDouble;
import org.hibernate.SessionFactory;

import java.util.Date;
import java.util.TimerTask;

public class AutoMeasurement extends TimerTask {

    private DrillDAO drillManager = DrillDAO.getDrillDAOInstance();
    private MeasurementDAO measurementManager = MeasurementDAO.getMeasurementDAOSharedInstance();
    private PollutantDAO pollutantManager = PollutantDAO.getPollutantDAOInstance();
    private RandomGaussianDouble gaussianGenerator = RandomGaussianDouble.getInstance();

    public AutoMeasurement(SessionFactory hibernateSessionFactory) {
        this.drillManager.setDrillSessionFactory(hibernateSessionFactory);
        this.measurementManager.setMeasurementSessionFactory(hibernateSessionFactory);
        this.pollutantManager.setPollutantSessionFactory(hibernateSessionFactory);
    }

    @Override
    public void run() {
        writeMeasurements(countPollutants(), countDrills(), getPositiveDouble());
    }

    private double getPositiveDouble() {
        double gauss = 0;
        boolean isPositive = false;
        while (isPositive == false) {
            gauss = gaussianGenerator.getGaussian();
            if (gauss >= 0) {
                isPositive = true;
            }
        }
        return gauss;
    }

    private int countPollutants() {
        return pollutantManager.getAllPollutants().toArray().length;
    }

    private int countDrills() {
        return drillManager.getAllDrills().toArray().length;
    }

    private void writeMeasurements(int pollutantsNumber, int drillsNumber, double value) {
        for (int i = 1; i <= drillsNumber; i++) {
            for (int j = 1; j <= pollutantsNumber; j++) {
                measurementManager.createMeasurement(new Date(), i, j, value);
            }
        }
    }
}
