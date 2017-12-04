/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {

    private static SessionFactory factory;

    public static void main(String[] args) {
        try {
            factory = new Configuration().configure().buildSessionFactory();
            System.out.println("Factory created");
        } catch (Throwable e) {
            System.out.println("Factory error");
            e.printStackTrace();
            throw new ExceptionInInitializerError();
        }
    }
}
