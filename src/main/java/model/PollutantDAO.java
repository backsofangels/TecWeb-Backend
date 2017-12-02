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

    public PollutantDAO(SessionFactory factory) {
        if (pollutantSessionFactory == null) {
            this.pollutantSessionFactory = factory;
        }
    }

    public List<Pollutant> getAllPollutants() {
        Session retrieveSession = this.pollutantSessionFactory.openSession();
        Transaction retrieveTransaction = null;
        List<Pollutant> pollutantsQueryResult = new ArrayList<Pollutant>();

        try {
            retrieveTransaction = retrieveSession.beginTransaction();
            pollutantsQueryResult = retrieveSession.createQuery("FROM Pollutant").list();
        } catch (HibernateException getAllPollutantsRetrieveException) {
            if (retrieveTransaction != null) {
                System.out.println("getAllPollutants retrieve error");
                getAllPollutantsRetrieveException.printStackTrace();
            }
        } finally {
            retrieveSession.close();
        }
        return pollutantsQueryResult;
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
            System.out.println("createPollutant writing error");
            createPollutantWritingException.printStackTrace();
        } finally {
            writingSession.close();
        }
        return pollutantID;
    }
}
