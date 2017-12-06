/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java.controller;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import main.java.utilities.LoginStatus;
import main.java.utilities.PasswordAuthentication;
import main.java.utilities.RegistrationStatus;
import main.java.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

public class AuthenticationController {
    private UserDAO userManager = UserDAO.getUserDAOInstance();
    private SessionFactory hibernateSessionFactory = null;
    private PasswordAuthentication authenticator = new PasswordAuthentication();

    public AuthenticationController() {
        try {
            hibernateSessionFactory = new Configuration().configure().buildSessionFactory();
            System.out.println("Factory created");
        } catch (Throwable e) {
            System.out.println("Factory error");
            e.printStackTrace();
            throw new ExceptionInInitializerError();
        }
    }

    public RegistrationStatus userRegistration(String firstName, String lastName, String email,
                                               char[] pwd, boolean isAdmin, int favoriteDrill) {
        RegistrationStatus outcome = null;
        userManager.setUserSessionFactory(hibernateSessionFactory);
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setHashedPwd(authenticator.hash(pwd));
        newUser.setAdminGrants(isAdmin);
        newUser.setFavoriteDrill(favoriteDrill);
        Integer newUserID = userManager.createUser(newUser);
        if (newUserID != null) {
            outcome = RegistrationStatus.SUCCEEDED;
        } else {
            outcome = RegistrationStatus.FAILED;
        }
        return outcome;
    }

    public Tuple loginHandler(String userEmail, String userHashedPwd) {
        String jwtToken = null;
        LoginStatus outcome = null;
        userManager.setUserSessionFactory(hibernateSessionFactory);
        User getUserFromDB = userManager.retrieveUserInfosByEmail(userEmail);

        if (getUserFromDB != null) {
            if (authenticator.authenticate(userHashedPwd.toCharArray(), getUserFromDB.getHashedPwd())) {
                outcome = LoginStatus.SUCCEDED;
                jwtToken = generateJWT(getUserFromDB);
            } else outcome = LoginStatus.FAILED;
        } else outcome = LoginStatus.NOT_REGISTERED;

        return new Tuple(outcome, jwtToken);
    }

    private String generateJWT(User userInformations) {
        String token = null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, 1);

        try {
            Algorithm hashingAlgorythm = Algorithm.HMAC256("secret");
            token = JWT.create()
                    .withIssuer("pollutech.com")
                    .withClaim("identifier", userInformations.getUserID())
                    .withClaim("firstName", userInformations.getFirstName())
                    .withClaim("lastName", userInformations.getLastName())
                    .withClaim("email", userInformations.getEmail())
                    .withClaim("favoriteDrill", userInformations.getFavoriteDrill())
                    .withExpiresAt(cal.getTime())
                    .sign(hashingAlgorythm);
        } catch(UnsupportedEncodingException unicodeException) {
            System.out.println("UTF Exception");
            unicodeException.printStackTrace();
        } catch (JWTCreationException JWTCreation) {
            System.out.println("JWT creation exception");
        }
        return token;
    }
}
