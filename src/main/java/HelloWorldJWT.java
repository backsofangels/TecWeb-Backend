/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java;

import static spark.Spark.*;
import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Base64;


public class HelloWorldJWT {
    public static void fuffa(String[] args) {
        port(8080);

        post("auth/jwt", (req, res) -> {
            String token = req.cookie("jwt");
            String decodedToken = "No decoded token";
            try {
                Algorithm hashingAlgorithm = Algorithm.HMAC256("secret");
                JWTVerifier verifier = JWT.require(hashingAlgorithm)
                        .withIssuer("salvini")
                        .build();
                DecodedJWT jwt = verifier.verify(token);
                String datas = jwt.getPayload();
                byte[] decoded = Base64.getDecoder().decode(jwt.getPayload());
                decodedToken = new String(decoded);
            } catch (UnsupportedEncodingException exception) {
                System.out.print("No right encoding");
            } catch (JWTVerificationException exception) {
                System.out.print("JWTVerificationException");
            }
            return decodedToken;
        });

        get("auth/getJWT", (req, res) -> {
            String token = "no token";
            try {
                Algorithm alg = Algorithm.HMAC256("secret");
                token = JWT.create()
                        .withIssuer("salvini")
                        .withClaim("nome", "salvatore")
                        .withClaim("cognome", "penitente")
                        .sign(alg);
                System.out.print("Token created");
            } catch (UnsupportedEncodingException UTFException) {
                System.out.print("UTFException");
            } catch (JWTCreationException JWTCreation) {
                System.out.print("JWTCreation exception");
            }
            res.cookie("jwt", token);
            return res;
        });
    }
}
