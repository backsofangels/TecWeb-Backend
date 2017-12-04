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
            retrieveTransaction.commit();
        } catch (HibernateException retrieveAllUsersRetrieveException) {
            if (retrieveTransaction != null) {
                retrieveTransaction.rollback();
            }
            System.out.println("retrieveAllUsers retrieve exception");
            retrieveAllUsersRetrieveException.printStackTrace();
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
            if (createTransaction != null) {
                createTransaction.rollback();
            }
            System.out.println("createUser creation exception");
            createUserCreateException.printStackTrace();
        } finally {
            createSession.close();
        }
        return userID;
    }

    public void updateUser (Integer userID, User updatedUser) {
        Session updateSession = userSessionFactory.openSession();
        Transaction updateTransaction = null;

        try {
            updateTransaction = updateSession.beginTransaction();
            User userToUpdate = (User) updateSession.get(User.class, userID);
            userToUpdate.setFirstName(updatedUser.getFirstName());
            userToUpdate.setLastName(updatedUser.getLastName());
            userToUpdate.setEmail(updatedUser.getEmail());
            userToUpdate.setFavLocationX(updatedUser.getFavLocationX());
            userToUpdate.setFavLocationY(updatedUser.getFavLocationY());
            updateSession.update(userToUpdate);
            updateTransaction.commit();
        } catch (HibernateException updateUserUpdateException) {
            if (updateTransaction != null) {
                updateTransaction.rollback();
            }
            System.out.println("updateUser update error");
            updateUserUpdateException.printStackTrace();
        } finally {
            updateSession.close();
        }
    }

    public void deleteUser (Integer userID) {
        Session deleteSession = userSessionFactory.openSession();
        Transaction deleteTransaction = null;

        try {
            deleteTransaction = deleteSession.beginTransaction();
            User userToDelete = (User) deleteSession.get(User.class, userID);
            deleteSession.delete(userToDelete);
            deleteTransaction.commit();
        } catch (HibernateException deleteUserDeleteException) {
            if (deleteTransaction != null) {
                deleteTransaction.rollback();
            }
            System.out.println("deleteUser delete error");
            deleteUserDeleteException.printStackTrace();
        } finally {
            deleteSession.close();
        }
    }
}
