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

public class PollutantDAO {
    private SessionFactory pollutantSessionFactory;
    private static PollutantDAO instance = new PollutantDAO();

    private PollutantDAO () {}

    public static PollutantDAO getPollutantDAOInstance() {
        return instance;
    }

    public void setPollutantSessionFactory(SessionFactory factory) {
        this.pollutantSessionFactory = factory;
    }

    public SessionFactory getPollutantSessionFactory() {
        return pollutantSessionFactory;
    }

    public List<Pollutant> getAllPollutants() {
        Session retrieveSession = this.pollutantSessionFactory.openSession();
        Transaction retrieveTransaction = null;
        List<Pollutant> pollutantsQueryResult = new ArrayList<Pollutant>();

        try {
            retrieveTransaction = retrieveSession.beginTransaction();
            pollutantsQueryResult = retrieveSession.createQuery("FROM Pollutant").list();
            retrieveTransaction.commit();
        } catch (HibernateException getAllPollutantsRetrieveException) {
            if (retrieveTransaction != null) {
                retrieveTransaction.rollback();
            }
            System.out.println("getAllPollutants retrieve error");
            getAllPollutantsRetrieveException.printStackTrace();
        } finally {
            retrieveSession.close();
        }
        return pollutantsQueryResult;
    }

    public Pollutant getPollutantByIdentifier (Integer pollutantID) {
        Session retrieveSession = this.pollutantSessionFactory.openSession();
        Transaction retrieveTransaction = null;
        Pollutant queryResult = null;

        try {
            retrieveTransaction = retrieveSession.beginTransaction();
            queryResult = (Pollutant) retrieveSession.get(Pollutant.class, pollutantID);
            retrieveTransaction.commit();
        } catch (HibernateException getPollutantByIDException) {
            if (retrieveTransaction != null) {
                retrieveTransaction.rollback();
            }
            getPollutantByIDException.printStackTrace();
        } finally {
            retrieveSession.close();
        }
        return queryResult;
    }

    public Integer createPollutant(String pollutantName, double pollutantMaximumThreshold) {
        Session writingSession = this.pollutantSessionFactory.openSession();
        Transaction writeTransaction = null;
        Integer pollutantID = null;

        try {
            writeTransaction = writingSession.beginTransaction();
            Pollutant pollutantToStore = new Pollutant(pollutantName, pollutantMaximumThreshold);
            pollutantID = (Integer) writingSession.save(pollutantToStore);
            writeTransaction.commit();
        } catch (HibernateException createPollutantWritingException) {
            if (writeTransaction != null) {
                writeTransaction.rollback();
            }
            System.out.println("createPollutant writing error");
            createPollutantWritingException.printStackTrace();
        } finally {
            writingSession.close();
        }
        return pollutantID;
    }

    public void updatePollutant (Integer pollutantID, Pollutant updatedPollutant) {
        Session updateSession = pollutantSessionFactory.openSession();
        Transaction updateTransaction = null;

        try {
            updateTransaction = updateSession.beginTransaction();
            Pollutant pollutantToUpdate = (Pollutant) updateSession.get(Pollutant.class, pollutantID);
            pollutantToUpdate.setPollutantName(updatedPollutant.getPollutantName());
            pollutantToUpdate.setMaximumThreshold(updatedPollutant.getMaximumThreshold());
            updateSession.update(pollutantToUpdate);
            updateTransaction.commit();
        } catch (HibernateException updatePollutantUpdateException) {
            if (updateTransaction != null) {
                updateTransaction.rollback();
            }
            System.out.println("updatePollutant update error");
            updatePollutantUpdateException.printStackTrace();
        } finally {
            updateSession.close();
        }
    }

    public void deletePollutant (Integer pollutantID) {
        Session deleteSession = pollutantSessionFactory.openSession();
        Transaction deleteTransaction = null;

        try {
            deleteTransaction = deleteSession.beginTransaction();
            Pollutant pollutantToDelete = (Pollutant) deleteSession.get(Pollutant.class, pollutantID);
            deleteSession.delete(pollutantToDelete);
            deleteTransaction.commit();
        } catch (HibernateException deletePollutantDeleteException) {
            if (deleteTransaction != null) {
                deleteTransaction.rollback();
            }
            System.out.println("deletePollutant delete error");
            deletePollutantDeleteException.printStackTrace();
        } finally {
            deleteSession.close();
        }
    }
}
