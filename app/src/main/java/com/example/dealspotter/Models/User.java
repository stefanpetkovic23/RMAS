package com.example.dealspotter.Models;

public class User {

    public String firstname,lastname,username,phonenumber,photoUri;

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer points;

    public User() {}

    public User(String firstname, String lastname, String username, String phonenumber, String photoUri,Integer points) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.phonenumber = phonenumber;
        this.photoUri = photoUri;
        this.points = points;
    }
    public User(String firstname, String lastname, String username, String phonenumber) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.phonenumber = phonenumber;

    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getUsername() {
        return username;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }
}
