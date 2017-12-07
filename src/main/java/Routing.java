/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java;
import static spark.Spark.*;

import main.java.controller.SearchController;
import main.java.utilities.ContentTypes;

public class Routing {

    private static SearchController searching = new SearchController();

    public static void main(String[] args) {
        port(8080);

        path("/api", () -> {
            path("/get", () -> {
                get("/drill/all", (request, response) -> {
                    response.type(ContentTypes.JSON.type());
                    return searching.getAllDrills();
                });
            });
        });
    }
}
