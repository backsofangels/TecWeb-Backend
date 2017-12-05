/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java;

import main.java.controller.SearchController;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {

    public static void main(String[] args) {
        SearchController search = new SearchController();
        search.getLatestMeasurementsByDrill(1);
    }
}
