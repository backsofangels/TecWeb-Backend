/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java.controller;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import main.java.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ManagementController {
    private SessionFactory hibernateSessionFactory;
    private DrillDAO drillManager = DrillDAO.getDrillDAOInstance();
    private MeasurementDAO measurementManager = MeasurementDAO.getMeasurementDAOSharedInstance();
    private PollutantDAO pollutantManager = PollutantDAO.getPollutantDAOInstance();
    private Gson jsonCreator = new Gson();
    private JsonParser jsonParser = new JsonParser();

    public ManagementController() {
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

    public String addDrill(String drillJSON) {
        System.out.println("Tua nonna");
        JsonObject drillJsonObject = jsonParser.parse(drillJSON).getAsJsonObject();
        double xCoordinate = drillJsonObject.get("xCoordinate").getAsDouble();
        double yCoordinate = drillJsonObject.get("yCoordinate").getAsDouble();
        Integer drillID = drillManager.createDrill(xCoordinate, yCoordinate);
        String drillInformations;
        if (drillID != null) {
            drillInformations = jsonCreator.toJson(drillManager.getDrillByID(drillID));
        } else {
            drillInformations = null;
        }
        return drillInformations;
    }

    public String updateDrill(String drillJSON) {
        JsonObject drillJsonObject = jsonParser.parse(drillJSON).getAsJsonObject();
        Drill drillToUpdate = new Drill();
        Integer drillIdentifier = drillJsonObject.get("drillIdentifier").getAsInt();
        drillToUpdate.setxCoordinate(drillJsonObject.get("xCoordinate").getAsDouble());
        drillToUpdate.setyCoordinate(drillJsonObject.get("yCoordinate").getAsDouble());
        drillToUpdate.setdrillID(drillIdentifier);
        drillManager.updateDrill(drillIdentifier, drillToUpdate);
        return jsonCreator.toJson(drillManager.getDrillByID(drillIdentifier));
    }

    public void deleteDrill(Integer drillIdentifier) {
        drillManager.deleteDrill(drillIdentifier);
    }

    public String addPollutant(String pollutantJSON) {
        JsonObject pollutantJsonObject = jsonParser.parse(pollutantJSON).getAsJsonObject();
        String pollutantName = pollutantJsonObject.get("pollutantName").getAsString();
        double maximumThreshold = pollutantJsonObject.get("maximumThreshold").getAsDouble();
        Integer pollutantID = pollutantManager.createPollutant(pollutantName, maximumThreshold);
        String pollutantInformations;
        if(pollutantID != null) {
            pollutantInformations = jsonCreator.toJson(pollutantManager.getPollutantByIdentifier(pollutantID));
        } else {
            pollutantInformations = null;
        }
        return pollutantInformations;
    }

    public String updatePollutant(String pollutantJSON) {
        JsonObject pollutantJSONObject = jsonParser.parse(pollutantJSON).getAsJsonObject();
        Pollutant pollutantToUpdate = new Pollutant(
                pollutantJSONObject.get("pollutantName").getAsString(),
                pollutantJSONObject.get("maximumThreshold").getAsDouble()
        );
        pollutantManager.updatePollutant(pollutantJSONObject.get("pollutantIdentifier").getAsInt(), pollutantToUpdate);
        Pollutant updatedPollutant = pollutantManager.getPollutantByIdentifier(pollutantToUpdate.getPollutantID());
        return jsonCreator.toJson(updatedPollutant);
    }

    public void deletePollutant(Integer pollutantIdentifier) {
        pollutantManager.deletePollutant(pollutantIdentifier);
    }

    public String createMeasurement(String measurementJSON) throws ParseException {
        JsonObject measurementJsonObject = jsonParser.parse(measurementJSON).getAsJsonObject();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date measurementDate = sdf.parse(measurementJsonObject.get("measurementDate").getAsString());
        int drillIdentifier = measurementJsonObject.get("drillIdentifier").getAsInt();
        int pollutantIdentifier = measurementJsonObject.get("pollutantIdentifier").getAsInt();
        double quantity = measurementJsonObject.get("quantity").getAsDouble();
        Integer measurementIdentifier = measurementManager.createMeasurement(measurementDate, drillIdentifier, pollutantIdentifier, quantity);
        String measurementInformations;
        if (measurementIdentifier != null) {
            measurementInformations = jsonCreator.toJson(measurementManager.getMeasurementByIdentifier(measurementIdentifier));
        } else {
            measurementInformations = null;
        }
        return measurementInformations;
    }

    public String updateMeasurement (String measurementJSON) throws ParseException {
        JsonObject measurementJsonObject = jsonParser.parse(measurementJSON).getAsJsonObject();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date measurementDate = sdf.parse(measurementJsonObject.get("measurementDate").getAsString());
        int drillIdentifier = measurementJsonObject.get("drillIdentifier").getAsInt();
        int pollutantIdentifier = measurementJsonObject.get("pollutantIdentifier").getAsInt();
        double quantity = measurementJsonObject.get("quantity").getAsDouble();
        int measurementIdentifier = measurementJsonObject.get("measurementIdentifier").getAsInt();
        Measurement updatedMeasurement = new Measurement(measurementDate, drillIdentifier, pollutantIdentifier, quantity);
        measurementManager.updateMeasurement(measurementIdentifier, updatedMeasurement);
        return jsonCreator.toJson(measurementManager.getMeasurementByIdentifier(measurementIdentifier));
    }

    public void deleteMeasurement(int measurementIdentifier) {
        measurementManager.deleteMeasurement(measurementIdentifier);
    }
}
