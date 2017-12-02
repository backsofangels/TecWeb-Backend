/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java.model;

public class User {


    private int userID;
    private String firstName;
    private String lastName;
    private String email;
    private String hashedPwd;
    private boolean adminGrants;
    private double favLocationX;
    private double favLocationY;

    public User() {}

    public User(int userID, String firstName, String lastName, String email,
                String hashedPwd, boolean adminGrants, double favLocationX, double favLocationY) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.hashedPwd = hashedPwd;
        this.adminGrants = adminGrants;
        this.favLocationX = favLocationX;
        this.favLocationY = favLocationY;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPwd() {
        return hashedPwd;
    }

    public void setHashedPwd(String hashedPwd) {
        this.hashedPwd = hashedPwd;
    }

    public boolean isAdminGrants() {
        return adminGrants;
    }

    public void setAdminGrants(boolean adminGrants) {
        this.adminGrants = adminGrants;
    }

    public double getFavLocationX() {
        return this.favLocationX;
    }

    public void setFavLocationX(double favLocationX) {
        this.favLocationX = favLocationX;
    }

    public double getFavLocationY() {
        return this.favLocationY;
    }

    public void setFavLocationY(double favLocationY) {
        this.favLocationY = favLocationY;
    }

}
