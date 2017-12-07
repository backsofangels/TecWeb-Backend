/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java.controller;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import main.java.model.Tuple;
import main.java.model.User;
import main.java.model.UserDAO;
import main.java.utilities.AuthorizationLevels;
import main.java.utilities.LoginStatus;
import main.java.utilities.PasswordAuthentication;
import main.java.utilities.RegistrationStatus;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

public class AuthenticationController {
    private UserDAO userManager = UserDAO.getUserDAOInstance();
    private SessionFactory hibernateSessionFactory;
    private PasswordAuthentication authenticator = new PasswordAuthentication();
    private Gson jsonManager = new Gson();

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
            Algorithm hashingAlgorithm = Algorithm.HMAC256("secret");
            token = JWT.create()
                    .withIssuer("pollutech.com")
                    .withClaim("identifier", userInformations.getUserID())
                    .withClaim("firstName", userInformations.getFirstName())
                    .withClaim("lastName", userInformations.getLastName())
                    .withClaim("email", userInformations.getEmail())
                    .withClaim("favoriteDrill", userInformations.getFavoriteDrill())
                    .withExpiresAt(cal.getTime())
                    .sign(hashingAlgorithm);
        } catch(UnsupportedEncodingException unicodeException) {
            System.out.println("UTF Exception");
            unicodeException.printStackTrace();
        } catch (JWTCreationException JWTCreation) {
            System.out.println("JWT creation exception");
        }
        return token;
    }

    public Tuple grantAuthorization (String tokenString) {
        AuthorizationLevels authGrants = AuthorizationLevels.NOT_AUTHORIZED;
        String renewedJWT = null;
        String decodedPayload = null;
        DecodedJWT decodedToken = null;
        Tuple result = null;

        try {
            Algorithm hashingAlgorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(hashingAlgorithm)
                    .withIssuer("pollutech.com")
                    .build();
            decodedToken = verifier.verify(tokenString);
            byte[] decoded = Base64.getDecoder().decode(decodedToken.getPayload());
            decodedPayload = new String(decoded);
        } catch (UnsupportedEncodingException UTFException) {
            System.out.println("UTFException in token validation");
            UTFException.printStackTrace();
        } catch (JWTVerificationException VerificationException) {
            System.out.println("JWTVerificationException");
            VerificationException.printStackTrace();
        }

        if (decodedToken != null) {
            authGrants = AuthorizationLevels.USER;
            boolean admin = jsonManager.fromJson(decodedPayload, User.class).isAdminGrants();
            if (admin) {
                authGrants = AuthorizationLevels.ADMIN;
            }
        }

        switch (authGrants) {
            case ADMIN: result = new Tuple(AuthorizationLevels.ADMIN, generateJWT(jsonManager.fromJson(decodedPayload, User.class)));
                break;
            case NOT_AUTHORIZED: result = new Tuple(AuthorizationLevels.NOT_AUTHORIZED, null);
                break;
            case USER: result = new Tuple(AuthorizationLevels.USER, generateJWT(jsonManager.fromJson(decodedPayload, User.class)));
                break;
        }

        return result;
    }
}
