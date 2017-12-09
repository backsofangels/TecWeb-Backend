/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java.utilities;

public enum StatusCodes {
    OK("200"),
    BAD_REQUEST("400"),
    UNAUTHORIZED("401"),
    FORBIDDEN("403"),
    NOT_FOUND("404");

    private String statusCode;

    StatusCodes(String code) {
        this.statusCode = code;
    }

    public int code() {
        return Integer.parseInt(statusCode);
    }
}
