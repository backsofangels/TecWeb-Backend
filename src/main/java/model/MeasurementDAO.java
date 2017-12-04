/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java.model;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MeasurementDAO {
    private SessionFactory measurementSessionFactory;

    public MeasurementDAO(SessionFactory factory) {
        if (measurementSessionFactory == null) {
            this.measurementSessionFactory = factory;
        }
    }

    public List<Measurement> getAllMeasurements() {
        Session retrieveSession = this.measurementSessionFactory.openSession();
        Transaction retrieveTransaction = null;
        List<Measurement> measurementQueryResults = new ArrayList<>();

        try {
            retrieveTransaction = retrieveSession.beginTransaction();
            measurementQueryResults = retrieveSession.createQuery("FROM Measurement").list();
        } catch (HibernateException getAllMeasurementRetrieveException) {
            if (retrieveTransaction != null) {
                System.out.println("getAllMeasurement retrieve error");
                getAllMeasurementRetrieveException.printStackTrace();
            }
        } finally {
            retrieveSession.close();
        }

        return measurementQueryResults;
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
            System.out.println("createMeasurement writing error");
            createMeasurementWritingException.printStackTrace();
        } finally {
            writingSession.close();
        }
        return measurementID;
    }
}
