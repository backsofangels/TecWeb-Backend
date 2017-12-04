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

public class UserDAO {
    private SessionFactory userSessionFactory;

    public UserDAO (SessionFactory factory) {
        if (this.userSessionFactory == null) {
            this.userSessionFactory = factory;
        }
    }

    public List<User> retrieveAllUsers() {
        Session retrieveSession = this.userSessionFactory.openSession();
        Transaction retrieveTransaction = null;

        List<User> userQueryResult = new ArrayList<>();

        try {
            retrieveTransaction = retrieveSession.beginTransaction();
            userQueryResult = retrieveSession.createQuery("FROM User").list();
        } catch (HibernateException retrieveAllUsersRetrieveException) {
            if (retrieveTransaction != null) {
                System.out.println("retrieveAllUsers retrieve exception");
                retrieveAllUsersRetrieveException.printStackTrace();
            }
        } finally {
            retrieveSession.close();
        }
        return userQueryResult;
    }

    public Integer createUser(String firstName, String lastName, String email, String hashedPwd,
                              boolean adminGrants, double favLocationX, double favLocationY) {
        Session createSession = this.userSessionFactory.openSession();
        Transaction createTransaction = null;
        Integer userID = null;

        try {
            createTransaction = createSession.beginTransaction();
            User userToSave = new User(firstName, lastName, email, hashedPwd, adminGrants, favLocationX, favLocationY);
            userID = (Integer) createSession.save(userToSave);
            createTransaction.commit();
        } catch (HibernateException createUserCreateException) {
            if (createTransaction == null) {
                System.out.println("createUser creation exception");
                createUserCreateException.printStackTrace();
            }
        } finally {
            createSession.close();
        }
        return userID;
    }
}
