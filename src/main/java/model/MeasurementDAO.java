/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java.model;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.lang.reflect.Array;
import java.util.*;

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

    public Measurement getMeasurementByIdentifier(Integer measurementIdentifier) {
        Session retrieveSession = this.measurementSessionFactory.openSession();
        Transaction retrieveTransaction = null;
        Measurement queryResult = null;

        try {
            retrieveTransaction = retrieveSession.beginTransaction();
            queryResult = retrieveSession.get(Measurement.class, measurementIdentifier);
            retrieveTransaction.commit();
        } catch (HibernateException getMeasurementByIdentifierException) {
            if (retrieveTransaction != null) {
                retrieveTransaction.rollback();
            }
            getMeasurementByIdentifierException.printStackTrace();
        } finally {
            retrieveSession.close();
        }
        return queryResult;
    }

    public List<Double> getMeasurementByDays(Date beginDate, Date endDate, int drillID) {
        Session measurementDaysSession = this.measurementSessionFactory.openSession();
        Transaction retrieveTransaction = null;
        List<MeasurementAVG> queryResult = new ArrayList<MeasurementAVG>();
        List<Double> avg = new ArrayList<>();

        try {
            retrieveTransaction = measurementDaysSession.beginTransaction();
            Query query= measurementDaysSession.createQuery("select avg(quantityMeasured) FROM Measurement where drillID=:drillIdentifier and " +
                    "measurementDate between :beginDate and :endDate group by pollutantID")
                    .setParameter("beginDate", beginDate)
                    .setParameter("endDate", endDate)
                    .setParameter("drillIdentifier", drillID);
            avg = query.list();
            retrieveTransaction.commit();
        } catch (HibernateException getMeasurementByDaysException) {
            if (retrieveTransaction != null) {
                retrieveTransaction.rollback();
            }
            System.out.println("Error in getMeasurementByDays");
            getMeasurementByDaysException.printStackTrace();
        } finally {
            measurementDaysSession.close();
        }
        return avg;
    }

    public Integer createMeasurement(Date measurementDate, int drillID, int pollutantID, double quantityMeasured) {
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
