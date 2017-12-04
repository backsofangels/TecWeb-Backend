/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java.model;
import org.hibernate.*;

import java.util.ArrayList;
import java.util.List;

public class DrillDAO {
    private SessionFactory drillSessionFactory;
    private static DrillDAO instance = new DrillDAO();

    private DrillDAO() {}

    public DrillDAO getDrillDAOInstance() {
        return instance;
    }

    public void setDrillSessionFactory(SessionFactory drillSessionFactory) {
        this.drillSessionFactory = drillSessionFactory;
    }

    public SessionFactory getDrillSessionFactory() {
        return drillSessionFactory;
    }

    public List<Drill> getAllDrills() {
        Session retrieveSession = this.drillSessionFactory.openSession();
        Transaction retrieveTransaction = null;
        List drillsQueryResult = new ArrayList<Drill>();

        try {
            retrieveTransaction = retrieveSession.beginTransaction();
            drillsQueryResult = retrieveSession.createQuery("FROM Drill").list();

            retrieveTransaction.commit();
        } catch (HibernateException getAllDrillsRetrieveException) {
            if (retrieveTransaction != null) {
                retrieveTransaction.rollback();
            }
            System.out.println("getAllDrills reading error");
            getAllDrillsRetrieveException.printStackTrace();
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
            if (writeTransaction != null) {
                writeTransaction.rollback();
            }
            System.out.println("createDrill writing error");
            createDrillWritingException.printStackTrace();
        } finally {
            writingSession.close();
        }
        return drillID;
    }

    public void updateDrill (Integer drillID, Drill updatedDrill) {
        Session updateSession = drillSessionFactory.openSession();
        Transaction updateTransaction = null;

        try {
            updateTransaction = updateSession.beginTransaction();
            Drill drillToUpdate = (Drill) updateSession.get(Drill.class, drillID);
            drillToUpdate.setxCoordinate(updatedDrill.getxCoordinate());
            drillToUpdate.setyCoordinate(updatedDrill.getyCoordinate());
            updateSession.update(drillToUpdate);
            updateTransaction.commit();
        } catch (HibernateException updateDrillUpdateException) {
            if (updateTransaction != null) {
                updateTransaction.rollback();
            }
            System.out.println("updateDrill update error");
            updateDrillUpdateException.printStackTrace();
        } finally {
            updateSession.close();
        }
    }

    public void deleteDrill (Integer drillID) {
        Session deleteSession = drillSessionFactory.openSession();
        Transaction deleteTransaction = null;

        try {
            deleteTransaction = deleteSession.beginTransaction();
            Drill drillToDelete = (Drill) deleteSession.get(Drill.class, drillID);
            deleteSession.delete(drillToDelete);
            deleteTransaction.commit();
        } catch (HibernateException deleteDrillDeleteException) {
            if (deleteTransaction != null) {
                deleteTransaction.rollback();
            }
            System.out.println("deleteDrill delete error");
            deleteDrillDeleteException.printStackTrace();
        } finally {
            deleteSession.close();
        }
    }
}
