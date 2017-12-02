/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java.model;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class DrillDAO {
    private SessionFactory drillSessionFactory;

    public DrillDAO(SessionFactory factory) {
        if (drillSessionFactory == null) {
            this.drillSessionFactory = factory;
        }
    }

    public List<Drill> getAllDrills() {
        Session retrieveSession = this.drillSessionFactory.openSession();
        Transaction retrieveTransaction = null;
        List<Drill> drillsQueryResult = new ArrayList<Drill>();

        try {
            retrieveTransaction = retrieveSession.beginTransaction();
            drillsQueryResult = retrieveSession.createQuery("FROM Drill").list();
        } catch (HibernateException getAllDrillsRetrieveException) {
            if (retrieveTransaction != null) {
                System.out.println("getAllDrills reading error");
                getAllDrillsRetrieveException.printStackTrace();
            }
        } finally {
            retrieveSession.close();
        }
        return drillsQueryResult;
    }

    public Integer createDrill(double xCoordinate, double yCoordinate) {
        Session writingSession = this.drillSessionFactory.openSession();
        Transaction writeTransaction = null;
        Integer drillID = null;

        try {
            writeTransaction = writingSession.beginTransaction();
            Drill drillToStore = new Drill(xCoordinate, yCoordinate);
            drillID = (Integer) writingSession.save(drillToStore);
            writeTransaction.commit();
        } catch (HibernateException createDrillWritingException) {
            System.out.println("createDrill writing error");
            createDrillWritingException.printStackTrace();
        } finally {
            writingSession.close();
        }
        return drillID;
    }

    //TODO: implement the update and delete
}
