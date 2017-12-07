/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java.utilities;

public enum ContentTypes {
    JSON("application/json");

    private String contentType;

    ContentTypes(String type) {
        this.contentType = type;
    }

    public String type() {
        return contentType;
    }
}
