package com.example.dealspotter.Models;

public class Rating {

    String userID;

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    String propertyId;
    Float ratingValue;
    public Rating(){}

    public Rating(String userID, Float ratingValue, String propertyID) {
        this.userID = userID;
        this.ratingValue = ratingValue;
        this.propertyId = propertyID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Float getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Float ratingValue) {
        this.ratingValue = ratingValue;
    }
}
