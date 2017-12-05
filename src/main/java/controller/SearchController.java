/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java.controller;

import main.java.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.*;
import org.hibernate.mapping.Join;

public class SearchController {

    //DONE: Tutte le sonde presenti con le loro coordinate
    //DONE: Data una sonda, restituire i rilevamenti su quella sonda nelle ultime sei ore (AKA i più recenti) per ogni inquinante
    //TODO: Data una sonda, restituire la media dei rilevamenti dato un intervallo di giorni per ogni inquinante

    private DrillDAO drillManager = DrillDAO.getDrillDAOInstance();
    private MeasurementDAO measurementManager = MeasurementDAO.getMeasurementDAOSharedInstance();
    private PollutantDAO pollutantManager = PollutantDAO.getPollutantDAOInstance();
    private SessionFactory hibernateSessionFactory = null;
    private Gson jsonCreator = new Gson();

    public SearchController() {
        try {
            hibernateSessionFactory = new Configuration().configure().buildSessionFactory();
            System.out.println("Factory created");
        } catch (Throwable e) {
            System.out.println("Factory error");
            e.printStackTrace();
            throw new ExceptionInInitializerError();
        }
    }

    public String retrieveDrillByID(int drillIdentifier) {
        drillManager.setDrillSessionFactory(hibernateSessionFactory);
        Drill queryResult = drillManager.getDrillByID(drillIdentifier);
        return jsonCreator.toJson(queryResult);
    }

    public String getAllDrills() {
        drillManager.setDrillSessionFactory(hibernateSessionFactory);
        List<Drill> queryResult = drillManager.getAllDrills();
        return jsonCreator.toJson(queryResult);
    }

    public String getLatestMeasurementsByDrill(int drillIdentifier) {
        measurementManager.setMeasurementSessionFactory(hibernateSessionFactory);
        pollutantManager.setPollutantSessionFactory(hibernateSessionFactory);
        List<Pollutant> pollutants = pollutantManager.getAllPollutants();
        List<Measurement> measurementQueryResult = measurementManager.getLatestMeasurementsByDrill(drillIdentifier, pollutants.toArray().length);
        ArrayList<JoinedMeasurement> merging = measurementAndPollutantMerge(pollutants, measurementQueryResult);
        return jsonCreator.toJson(merging);
    }

    public ArrayList<JoinedMeasurement> measurementAndPollutantMerge (List<Pollutant> pollutantsToMerge, List<Measurement> measurementsToMerge) {
        ArrayList<JoinedMeasurement> mergeResult = new ArrayList<>();

        for (Measurement m:measurementsToMerge) {
            Pollutant pollutantMonitored = pollutantsToMerge.stream().filter(pollutant -> pollutant.getPollutantID() == m.getPollutantID()).findFirst().get();
            JoinedMeasurement mergedMeasurement = new JoinedMeasurement(m.getMeasurementDate(), pollutantMonitored, m.getQuantityMeasured());
            mergeResult.add(mergedMeasurement);
        }
        return mergeResult;
    }
}