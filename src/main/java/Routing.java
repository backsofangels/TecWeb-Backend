/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java;
import static spark.Spark.*;

import com.mysql.jdbc.util.Base64Decoder;
import main.java.controller.AuthenticationController;
import main.java.controller.SearchController;
import main.java.model.Tuple;
import main.java.utilities.*;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import java.util.*;

public class Routing {

    private static SearchController searching = new SearchController();
    private static AuthenticationController auth = new AuthenticationController();
    private static JsonParser jsonParser = new JsonParser();

    public static void main(String[] args) {
        port(8080);

        path("/api", () -> {
            path("/get", () -> {
                get("/drill/all", (request, response) -> {
                    response.type(ContentTypes.JSON.type());
                    return searching.getAllDrills();
                });

                get("/drill/info/:identifier", (request, response) -> {
                    int drillIdentifier = Integer.parseInt(request.params(":identifier"));
                    response.type(ContentTypes.JSON.type());
                    return searching.retrieveDrillByID(drillIdentifier);
                });

                get("/drill/measurement/:identifier", (request, response) -> {
                    int drillIdentifier = Integer.parseInt(request.params(":identifier"));
                    response.type(ContentTypes.JSON.type());
                    return searching.getLatestMeasurementsByDrill(drillIdentifier);
                });

                get("/drill/average", (request, response) -> {
                    System.out.println(request.toString());
                    int drillIdentifier = Integer.parseInt(request.queryParams("identifier"));
                    String beginningDate = request.queryParams("beginDate");
                    String endingDate = request.queryParams("endDate");
                    System.out.println(drillIdentifier);
                    System.out.println(beginningDate);
                    System.out.println(endingDate);
                    response.type(ContentTypes.JSON.type());
                    return searching.getMeasurementsAVG(drillIdentifier,beginningDate, endingDate);
                });

            });

            path("/auth", () -> {
                get("/login", (request, response) -> {
                    byte[] base64Combination = request.headers("Authorization").substring(6, request.headers("Authorization").length()).getBytes();
                    String decoded = base64Decoding(base64Combination);
                    final String[] combination = decoded.split(":", 2);
                    Tuple<LoginStatus, String> loginOutcome = auth.loginHandler(combination[0], combination[1]);
                    if (loginOutcome.getFirstTupleElement() == LoginStatus.SUCCEDED) {
                        response.cookie("jwt", loginOutcome.getSecondTupleElement());
                        response.status(StatusCodes.OK.code());
                    } else if (loginOutcome.getFirstTupleElement() == LoginStatus.FAILED) {
                        response.status(StatusCodes.UNAUTHORIZED.code());
                    } else if (loginOutcome.getFirstTupleElement() == LoginStatus.NOT_REGISTERED) {
                        response.status(StatusCodes.BAD_REQUEST.code());
                    }
                    return response;
                });

                post("/signup", (request, response) -> {
                    String requestBody = request.body();
                    JsonObject obj = jsonParser.parse(requestBody).getAsJsonObject();
                    String firstName = obj.get("firstName").getAsString();
                    String lastName = obj.get("lastName").getAsString();
                    String email = obj.get("email").getAsString();
                    char[] pwd = obj.get("pwd").getAsString().toCharArray();
                    int favDrill = obj.get("favoriteDrill").getAsInt();
                    RegistrationStatus outcome = auth.userRegistration(firstName, lastName, email, pwd, false, favDrill);
                    if (outcome == RegistrationStatus.FAILED) {
                        response.status(StatusCodes.BAD_REQUEST.code());
                    } else if (outcome == RegistrationStatus.USER_ALREADY_PRESENT) {
                        response.status(StatusCodes.FORBIDDEN.code());
                    } else if (outcome == RegistrationStatus.SUCCEEDED) {
                        response.status(StatusCodes.OK.code());
                    }
                    return response;
                });

                get("/me", (request, response) -> {
                    String token = request.cookie("jwt");
                    Tuple <AuthorizationLevels, String> outcome = auth.grantAuthorization(token);
                    if (outcome.getFirstTupleElement() == AuthorizationLevels.NOT_AUTHORIZED) {
                        response.status(StatusCodes.UNAUTHORIZED.code());
                    } else {
                        response.status(StatusCodes.OK.code());
                        response.cookie("jwt", outcome.getSecondTupleElement());
                    }
                    return response;
                });

                put("/update", (request, response) -> {
                    String requestBody = request.body();
                    String authToken = request.cookie("jwt");
                    boolean isTokenValid = auth.jwtValidation(authToken);
                    if (isTokenValid) {
                        JsonObject obj = jsonParser.parse(requestBody).getAsJsonObject();
                        System.out.println(obj.toString());
                        String firstName = obj.get("firstName").getAsString();
                        String lastName = obj.get("lastName").getAsString();
                        String email = obj.get("email").getAsString();
                        int favDrill = obj.get("favoriteDrill").getAsInt();
                        String newToken = auth.userUpdate(firstName, lastName, email, favDrill);
                        response.status(StatusCodes.OK.code());
                        response.cookie("jwt", newToken);
                    } else {
                        response.status(StatusCodes.UNAUTHORIZED.code());
                    }
                    return response;
                });
            });
        });
    }

    private static String base64Decoding(byte[] toDecode) {
        return new String(Base64.getDecoder().decode(toDecode));
    }
}
