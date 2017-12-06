/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java;

import main.java.controller.AuthenticationController;
import main.java.controller.SearchController;
import main.java.model.MeasurementAVG;
import main.java.model.Tuple;
import main.java.model.User;
import main.java.utilities.PasswordAuthentication;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.text.ParseException;

public class Main {

    public static void main(String[] args) throws IllegalArgumentException {
        AuthenticationController auth = new AuthenticationController();
        PasswordAuthentication passwordHasher = new PasswordAuthentication();
        String password = "buongiornissimo";
        char[] pwd = password.toCharArray();
/*        auth.userRegistration("Giangiacomo", "Fristulli","fristulli@gmail.com",
                pwd, false, 1);*/
        Tuple result = auth.loginHandler("fristulli@gmail.com", password);
        System.out.println("Third try, all right:" + result.toString());
    }
}
