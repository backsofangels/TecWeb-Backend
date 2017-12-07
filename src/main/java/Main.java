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
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZGVudGlmaWVyIjo2LCJmaXJzdE5hbWUiOiJHaWFuZ2lhY29tbyIsImxhc3ROYW1lIjoiRnJpc3R1bGxpIiwiZmF2b3JpdGVEcmlsbCI6MSwiaXNzIjoicG9sbHV0ZWNoLmNvbSIsImV4cCI6MTUxMjY0NzM1MCwiZW1haWwiOiJmcmlzdHVsbGlAZ21haWwuY29tIn0.ec0fGGm2pf3kinlUz0WSwx5OIZ33cc8T9kkvhd-kHeo";

        Tuple result = auth.grantAuthorization(token);
        System.out.println(result.toString());
    }
}
