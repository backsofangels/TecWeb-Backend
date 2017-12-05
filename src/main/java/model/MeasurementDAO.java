/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java.model;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MeasurementDAO {
    private SessionFactory measurementSessionFactory;
    private static MeasurementDAO instance = new MeasurementDAO();

    private MeasurementDAO() {}

    public static MeasurementDAO getMeasurementDAOSharedInstance() {
        return instance;
    }

    public void setMeasurementSessionFactory(SessionFactory factory) {
        this.measurementSessionFactory = factory;
    }

    public SessionFactory getMeasurementSessionFactory() {
        return measurementSessionFactory;
    }

    public List<Measurement> getAllMeasurements() {
        Session retrieveSession = this.measurementSessionFactory.openSession();
        Transaction retrieveTransaction = null;
        List<Measurement> measurementQueryResults = new ArrayList<>();

        try {
            retrieveTransaction = retrieveSession.beginTransaction();
            measurementQueryResults = retrieveSession.createQuery("FROM Measurement").list();
            retrieveTransaction.commit();
        } catch (HibernateException getAllMeasurementRetrieveException) {
            if (retrieveTransaction != null) {
                retrieveTransaction.rollback();
            }
            System.out.println("getAllMeasurement retrieve error");
            getAllMeasurementRetrieveException.printStackTrace();
        } finally {
            retrieveSession.close();
        }

        return measurementQueryResults;
    }

    public List<Measurement> getLatestMeasurementsByDrill(int drillIdentifier, int pollutantNumber) {
        Session retrieveSession = this.measurementSessionFactory.openSession();
        Transaction retrieveTransaction = null;
        List<Measurement> measurementResults = new ArrayList<Measurement>();

        try {
            retrieveTransaction = retrieveSession.beginTransaction();
            measurementResults = retrieveSession.createQuery("FROM Measurement where drillID=:drillIdentifier")
                    .setParameter("drillIdentifier", drillIdentifier)
                    .setMaxResults(pollutantNumber)
                    .list();
            retrieveTransaction.commit();
        } catch (HibernateException getLatestMeasurementsByDrillException) {
            if (retrieveTransaction != null) {
                retrieveTransaction.rollback();
            }
            System.out.println("getLatestMeasurementsByDrill error");
            getLatestMeasurementsByDrillException.printStackTrace();
        } finally {
            retrieveSession.close();
        }
        return measurementResults;
    }

    public Integer createMeasurement(Date measurementDate, int drillID, int pollutantID, int quantityMeasured) {
        Session writingSession = this.measurementSessionFactory.openSession();
        Transaction writeTransaction = null;
        Integer measurementID = null;

        try {
            writeTransaction = writingSession.beginTransaction();
            Measurement measurementToStore = new Measurement(measurementDate, drillID, pollutantID, quantityMeasured);
            measurementID = (Integer) writingSession.save(measurementToStore);
            writeTransaction.commit();
        } catch (HibernateException createMeasurementWritingException) {
            if (writeTransaction != null) {
                writeTransaction.rollback();
            }
            System.out.println("createMeasurement writing error");
            createMeasurementWritingException.printStackTrace();
        } finally {
            writingSession.close();
        }
        return measurementID;
    }

    public void updateMeasurement (Integer measurementID, Measurement updatedMeasurement) {
        Session updateSession = measurementSessionFactory.openSession();
        Transaction updateTransaction = null;

        try {
            updateTransaction = updateSession.beginTransaction();
            Measurement measurementToUpdate = (Measurement) updateSession.get(Measurement.class, measurementID);
            measurementToUpdate.setDrillID(updatedMeasurement.getDrillID());
            measurementToUpdate.setMeasurementDate(updatedMeasurement.getMeasurementDate());
            measurementToUpdate.setPollutantID(updatedMeasurement.getPollutantID());
            measurementToUpdate.setQuantityMeasured(updatedMeasurement.getQuantityMeasured());
            updateSession.update(measurementToUpdate);
            updateTransaction.commit();
        } catch (HibernateException updateMeasurementUpdateException) {
            if (updateTransaction != null) {
                updateTransaction.rollback();
            }
            System.out.println("updateMeasurement update error");
            updateMeasurementUpdateException.printStackTrace();
        } finally {
            updateSession.close();
        }
    }

    public void deleteMeasurement (Integer measurementID) {
        Session deleteSession = measurementSessionFactory.openSession();
        Transaction deleteTransaction = null;

        try {
            deleteTransaction = deleteSession.beginTransaction();
            Measurement measurementToDelete = (Measurement) deleteSession.get(Measurement.class, measurementID);
            deleteSession.delete(measurementToDelete);
            deleteTransaction.commit();
        } catch (HibernateException deleteMeasurementDeleteException) {
            if (deleteTransaction != null) {
                deleteTransaction.rollback();
            }
            System.out.println("deleteMeasurement delete error");
            deleteMeasurementDeleteException.printStackTrace();
        } finally {
            deleteSession.close();
        }
    }
}