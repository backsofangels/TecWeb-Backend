/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java.controller;

import com.google.gson.Gson;
import main.java.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SearchController {

    //DONE: Tutte le sonde presenti con le loro coordinate
    //DONE: Data una sonda, restituire i rilevamenti su quella sonda nelle ultime sei ore (AKA i più recenti) per ogni inquinante
    //DONE: Data una sonda, restituire la media dei rilevamenti dato un intervallo di giorni per ogni inquinante

    //TODO: Code refactoring and clearing
    //TODO: declare and instance manager sessionFactories in the constructor of SearchController

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

    public String getMeasurementsAVG() {
        measurementManager.setMeasurementSessionFactory(hibernateSessionFactory);
        pollutantManager.setPollutantSessionFactory(hibernateSessionFactory);
        drillManager.setDrillSessionFactory(hibernateSessionFactory);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        MeasurementAVG queryFinalResult = new MeasurementAVG();
        List<Double> avgsQueryResult = new ArrayList<>();
        List<Pollutant> pollutants = new ArrayList<>();

        try {
            Date beginDate =  sdf.parse("01/01/1990");
            Date endDate = sdf.parse("01/01/2020");
            avgsQueryResult = measurementManager.getMeasurementByDays(beginDate, endDate, 1);
            pollutants = pollutantManager.getAllPollutants();
            Drill drill = drillManager.getDrillByID(1);
            queryFinalResult.setDrillInformations(drill);
            queryFinalResult.setMinorBoundary(beginDate);
            queryFinalResult.setMaximumBoundary(endDate);
            queryFinalResult.setMeasurements(createPollutantAndAVGMap(avgsQueryResult, pollutants));
        } catch (ParseException dateParsingException) {
            dateParsingException.printStackTrace();
        }

        return jsonCreator.toJson(queryFinalResult);
    }

    public HashMap<Integer, Tuple> createPollutantAndAVGMap(List<Double> avgs, List<Pollutant> pollutants) {
        HashMap<Integer, Tuple> resultMap = new HashMap<>();
        ArrayList<Pollutant> polls = (ArrayList<Pollutant>) pollutants;
        ArrayList<Double> averages = (ArrayList<Double>) avgs;

        for (int i = 0; i<polls.size(); i++) {
            resultMap.put(i, new Tuple(polls.get(i), averages.get(i)));
        }

        return resultMap;
    }
}