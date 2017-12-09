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

    private DrillDAO drillManager = DrillDAO.getDrillDAOInstance();
    private MeasurementDAO measurementManager = MeasurementDAO.getMeasurementDAOSharedInstance();
    private PollutantDAO pollutantManager = PollutantDAO.getPollutantDAOInstance();
    private SessionFactory hibernateSessionFactory;
    private Gson jsonCreator = new Gson();

    public SearchController() {
        try {
            hibernateSessionFactory = new Configuration().configure().buildSessionFactory();
            drillManager.setDrillSessionFactory(hibernateSessionFactory);
            measurementManager.setMeasurementSessionFactory(hibernateSessionFactory);
            pollutantManager.setPollutantSessionFactory(hibernateSessionFactory);
            System.out.println("Factory created");
        } catch (Throwable e) {
            System.out.println("Factory error");
            e.printStackTrace();
            throw new ExceptionInInitializerError();
        }
    }


    public String getAllDrills() {
        List<Drill> queryResult = drillManager.getAllDrills();
        return jsonCreator.toJson(queryResult);
    }

    public String getAllPollutants() {
        List<Pollutant> queryResult = pollutantManager.getAllPollutants();
        return jsonCreator.toJson(queryResult);
    }

    public String getPollutantByIdentifier(Integer pollutantIdentifier) {
        return jsonCreator.toJson(pollutantManager.getPollutantByIdentifier(pollutantIdentifier));
    }

    public String getAllMeasurements() {
        return jsonCreator.toJson(measurementManager.getAllMeasurements());
    }

    public String getMeasurementByIdentifier(Integer measurementIdentifier) {
        return jsonCreator.toJson(measurementManager.getMeasurementByIdentifier(measurementIdentifier));
    }

    public String retrieveDrillByID(int drillIdentifier) {
        Drill queryResult = drillManager.getDrillByID(drillIdentifier);
        return jsonCreator.toJson(queryResult);
    }

    public String getLatestMeasurementsByDrill(int drillIdentifier) {
        List<Pollutant> pollutants = pollutantManager.getAllPollutants();
        List<Measurement> measurementQueryResult = measurementManager.getLatestMeasurementsByDrill(drillIdentifier, pollutants.toArray().length);
        ArrayList<JoinedMeasurement> merging = measurementAndPollutantMerge(pollutants, measurementQueryResult);
        return jsonCreator.toJson(merging);
    }

    public ArrayList<JoinedMeasurement> measurementAndPollutantMerge (List<Pollutant> pollutantsToMerge, List<Measurement> measurementsToMerge) {
        ArrayList<JoinedMeasurement> mergeResult = new ArrayList<>();

        //THIS ONE IS KINDA TRICKY TO EXPLAIN
        for (Measurement m:measurementsToMerge) {
            Pollutant pollutantMonitored = pollutantsToMerge
                    .stream() //conforms the pollutants list to the stream type
                    .filter(pollutant -> pollutant.getPollutantID() == m.getPollutantID()) //here happens the actual filtering
                    .findFirst() //finds the first instance present in the stream
                    .get(); //retrieves that instance
            JoinedMeasurement mergedMeasurement = new JoinedMeasurement(m.getMeasurementDate(), pollutantMonitored, m.getQuantityMeasured());
            mergeResult.add(mergedMeasurement);
        }
        return mergeResult;
    }

    public String getMeasurementsAVG(int drillIdentifier, String beginDateString, String endDateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        MeasurementAVG queryFinalResult = new MeasurementAVG();
        List<Double> avgsQueryResult;
        List<Pollutant> pollutants;

        try {
            Date beginDate =  sdf.parse(beginDateString);
            Date endDate = sdf.parse(endDateString);
            avgsQueryResult = measurementManager.getMeasurementByDays(beginDate, endDate, drillIdentifier);
            pollutants = pollutantManager.getAllPollutants();
            Drill drill = drillManager.getDrillByID(drillIdentifier);
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