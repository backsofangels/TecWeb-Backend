/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java;

import main.java.controller.SearchController;
import main.java.model.MeasurementAVG;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.text.ParseException;

public class Main {

    public static void main(String[] args) {
        SearchController search = new SearchController();
        MeasurementAVG.print(search.getMeasurementsAVG());
    }
}
