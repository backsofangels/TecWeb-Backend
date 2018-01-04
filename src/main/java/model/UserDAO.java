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
    private static UserDAO instance = new UserDAO();

    private UserDAO() {}

    public static UserDAO getUserDAOInstance() {
        return instance;
    }

    public void setUserSessionFactory(SessionFactory userSessionFactory) {
        this.userSessionFactory = userSessionFactory;
    }

    public SessionFactory getUserSessionFactory() {
        return userSessionFactory;
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

    public User retrieveUserInfos(int userIdentifier) {
        Session retrieveSession = this.userSessionFactory.openSession();
        Transaction retrieveTransaction = null;
        User queryResult = null;

        try {
            retrieveTransaction = retrieveSession.beginTransaction();
            queryResult = retrieveSession.get(User.class, userIdentifier);
            retrieveTransaction.commit();
        } catch (HibernateException retrieveUserException) {
            if (retrieveTransaction != null) {
                retrieveTransaction.rollback();
            }
            System.out.println("retrieveUser error");
            retrieveUserException.printStackTrace();
        } finally {
            retrieveSession.close();
        }
        return queryResult;
    }

    public User retrieveUserInfosByEmail(String email) {
        Session retrieveSession = this.userSessionFactory.openSession();
        Transaction retrieveTransaction = null;
        List queryResult = null;
        User userFound = null;

        try {
            retrieveTransaction = retrieveSession.beginTransaction();
            queryResult = retrieveSession.createQuery("FROM User where email=:email")
                    .setParameter("email", email)
                    .list();
            if (queryResult.toArray().length != 0) {
                userFound = (User) queryResult.get(0);
            }
            retrieveTransaction.commit();
        } catch (HibernateException retrieveUserException) {
            if (retrieveTransaction != null) {
                retrieveTransaction.rollback();
            }
            System.out.println("retrieveUserByEmail error");
            retrieveUserException.printStackTrace();
        } finally {
            retrieveSession.close();
        }
        return userFound;
    }

    public void updatePassword(String hashedPwd, int userID) {
        Session updateSession = userSessionFactory.openSession();
        Transaction updateTransaction = null;
        System.out.println("Sto updatando la password perchè sono bello");
        try {
            updateTransaction = updateSession.beginTransaction();
            User userToUpdate = (User) updateSession.get(User.class, userID);
            System.out.println("La password dell'utente " + userToUpdate.getEmail() + " prima dell'update è " + userToUpdate.getHashedPwd());
            userToUpdate.setHashedPwd(hashedPwd);
            System.out.println("La password dell'utente " + userToUpdate.getEmail() + " dopo dell'update è " + userToUpdate.getHashedPwd());
            updateSession.update(userToUpdate);
            System.out.println("O almeno ci sto provando :(((");
            updateTransaction.commit();
        } catch (HibernateException updateUserUpdateException) {
            if (updateTransaction != null) {
                updateTransaction.rollback();
            }
            System.out.println("updateUserPassword update error");
            updateUserUpdateException.printStackTrace();
        } finally {
            updateSession.close();
        }
    }


    public Integer createUser(User userToSave) {
        Session createSession = this.userSessionFactory.openSession();
        Transaction createTransaction = null;
        Integer userID = null;

        try {
            createTransaction = createSession.beginTransaction();
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
            userToUpdate.setFavoriteDrill(updatedUser.getFavoriteDrill());
            userToUpdate.setHashedPwd(updatedUser.getHashedPwd());
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
