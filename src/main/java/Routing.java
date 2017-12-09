/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java;

import static spark.Spark.*;
import main.java.controller.AuthenticationController;
import main.java.controller.ManagementController;
import main.java.controller.SearchController;
import main.java.model.Tuple;
import main.java.utilities.*;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import java.util.*;

public class Routing {

    //Utilities for the class
    private static SearchController searching = new SearchController();
    private static AuthenticationController auth = new AuthenticationController();
    private static ManagementController management = new ManagementController();
    private static JsonParser jsonParser = new JsonParser();

    public static void main(String[] args) {
        //Port setting for the server, if needed another port change this one
        port(8080);

        path("/api", () -> {
            path("/get", () -> {

                //GET request
                //Retrieves all the drill informations
                get("/drill/all", (request, response) -> {
                    response.type(ContentTypes.JSON.type());
                    return searching.getAllDrills();
                });

                //GET request
                //Retrieve a single drill infos given its identifier
                get("/drill/info/:identifier", (request, response) -> {
                    int drillIdentifier = Integer.parseInt(request.params(":identifier"));
                    response.type(ContentTypes.JSON.type());
                    return searching.retrieveDrillByID(drillIdentifier);
                });

                //GET request
                //Retrieve a single drill latest measurements given its identifier
                get("/drill/measurement/:identifier", (request, response) -> {
                    int drillIdentifier = Integer.parseInt(request.params(":identifier"));
                    response.type(ContentTypes.JSON.type());
                    return searching.getLatestMeasurementsByDrill(drillIdentifier);
                });

                //GET REQUEST
                //Retrieve a drill's measurement average given an identifier, a begin date and an end date
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

                get("/pollutants/all", (request, response) -> {
                    response.status(StatusCodes.OK.code());
                    return searching.getAllPollutants();
                });

                get("/pollutants/info/:identifier", (request, response) -> {
                    int pollutantIdentifier = Integer.parseInt(request.params(":identifier"));
                    response.type(ContentTypes.JSON.type());
                    return searching.getPollutantByIdentifier(pollutantIdentifier);
                });

                get("/measurements/all", (request, response) -> {
                    return searching.getAllMeasurements();
                });

                get("/measurements/info/:identifier", (request, response) -> {
                    response.type(ContentTypes.JSON.type());
                    return searching.getPollutantByIdentifier(Integer.parseInt(request.params(":identifier")));
                });
            });

            path("/add", () -> {
                post("/drill", (request, response) -> {
                    String token = request.cookie("jwt");
                    Tuple outcome = auth.grantAuthorization(token);
                    if (outcome.getFirstTupleElement() == AuthorizationLevels.ADMIN) {
                        String newDrill = management.addDrill(response.body());
                        response.type(ContentTypes.JSON.type());
                        response.body(newDrill);
                    } else {
                        response.status(StatusCodes.UNAUTHORIZED.code());
                    }
                    return response;
                });

                post("/pollutant", (request, response) -> {
                    String token = request.cookie("jwt");
                    Tuple outcome = auth.grantAuthorization(token);
                    if (outcome.getFirstTupleElement() == AuthorizationLevels.ADMIN) {
                        String newPollutant = management.addPollutant(response.body());
                        response.type(ContentTypes.JSON.type());
                        response.body(newPollutant);
                    } else {
                        response.status(StatusCodes.UNAUTHORIZED.code());
                    }
                    return response;
                });

                post("/measurement", (request, response) -> {
                    String token = request.cookie("jwt");
                    Tuple outcome = auth.grantAuthorization(token);
                    if (outcome.getFirstTupleElement() == AuthorizationLevels.ADMIN) {
                        String newMeasurement = management.createMeasurement(response.body());
                        response.type(ContentTypes.JSON.type());
                        response.body(newMeasurement);
                    } else {
                        response.status(StatusCodes.UNAUTHORIZED.code());
                    }
                    return response;
                });
            });

            path("/update", () -> {
                put("/drill", (request, response) -> {
                    String token = request.cookie("jwt");
                    Tuple outcome = auth.grantAuthorization(token);

                    if (outcome.getFirstTupleElement() == AuthorizationLevels.ADMIN) {
                        String updatedDrill = management.updateDrill(request.body());
                        response.type(ContentTypes.JSON.type());
                        response.body(updatedDrill);
                    } else {
                        response.status(StatusCodes.UNAUTHORIZED.code());
                    }
                    return response;
                });

                put("/drill", (request, response) -> {
                    String token = request.cookie("jwt");
                    Tuple outcome = auth.grantAuthorization(token);

                    if (outcome.getFirstTupleElement() == AuthorizationLevels.ADMIN) {
                        String updatedPollutant = management.updatePollutant(request.body());
                        response.type(ContentTypes.JSON.type());
                        response.body(updatedPollutant);
                    } else {
                        response.status(StatusCodes.UNAUTHORIZED.code());
                    }
                    return response;
                });

                put("/measurement", (request, response) -> {
                    String token = request.cookie("jwt");
                    Tuple outcome = auth.grantAuthorization(token);

                    if (outcome.getFirstTupleElement() == AuthorizationLevels.ADMIN) {
                        String updatedMeasurement = management.updateMeasurement(request.body());
                        response.type(ContentTypes.JSON.type());
                        response.body(updatedMeasurement);
                    } else {
                        response.status(StatusCodes.UNAUTHORIZED.code());
                    }
                    return response;
                });
            });

            path("/delete", () -> {
               put("/drill/:identifier", (request, response) -> {
                   String token = request.cookie("jwt");
                   Tuple outcome = auth.grantAuthorization(token);

                   if (outcome.getFirstTupleElement() == AuthorizationLevels.ADMIN) {
                       management.deleteDrill(Integer.valueOf(request.params(":identifier")));
                       response.body("" + StatusCodes.OK.code());
                   } else {
                       response.status(StatusCodes.OK.code());
                   }
                   return response;
               });


                put("/pollutant/:identifier", (request, response) -> {
                    String token = request.cookie("jwt");
                    Tuple outcome = auth.grantAuthorization(token);

                    if (outcome.getFirstTupleElement() == AuthorizationLevels.ADMIN) {
                        management.deletePollutant(Integer.valueOf(request.params(":identifier")));
                        response.body("" + StatusCodes.OK.code());
                    } else {
                        response.status(StatusCodes.OK.code());
                    }
                    return response;
                });

                put("/measurement/:identifier", (request, response) -> {
                    String token = request.cookie("jwt");
                    Tuple outcome = auth.grantAuthorization(token);

                    if (outcome.getFirstTupleElement() == AuthorizationLevels.ADMIN) {
                        management.deleteMeasurement(Integer.valueOf(request.params(":identifier")));
                        response.body("" + StatusCodes.OK.code());
                    } else {
                        response.status(StatusCodes.OK.code());
                    }
                    return response;
                });

            });

            path("/auth", () -> {

                //GET REQUEST
                //Login endpoint
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

                //POST request
                //Registration endpoint
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

                //GET request
                //Retrieves the user profile informations
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

                //PUT request
                //Updates the user informations
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

    //Utility function that decodes a base64 encoded string to a normal string
    private static String base64Decoding(byte[] toDecode) {
        return new String(Base64.getDecoder().decode(toDecode));
    }
}
