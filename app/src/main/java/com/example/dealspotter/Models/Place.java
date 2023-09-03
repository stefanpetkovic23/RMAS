package com.example.dealspotter.Models;

import java.util.HashMap;
import java.util.Map;

public class Place {
    String adress,owner,description,title,photoURL,type;
    Double latitude;
    Double longitude;
    Integer numberofrooms,numberofbathrooms,price,garage,surface;
    Map<String,Rating> rating;

    public Place(){}

    public Place(String adress, String owner, String description, String title, String photoURL, Double latitude, Double longitude, Integer numberofrooms, Integer numberofbathrooms, Integer price, Integer garage, Integer surface) {
        this.adress = adress;
        this.owner = owner;
        this.description = description;
        this.title = title;
        this.photoURL = photoURL;
        this.latitude = latitude;
        this.longitude = longitude;
        this.numberofrooms = numberofrooms;
        this.numberofbathrooms = numberofbathrooms;
        this.price = price;
        this.garage = garage;
        this.surface = surface;
        this.rating = new HashMap<>();
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }



    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Integer getNumberofrooms() {
        return numberofrooms;
    }

    public void setNumberofrooms(Integer numberofrooms) {
        this.numberofrooms = numberofrooms;
    }

    public Integer getNumberofbathrooms() {
        return numberofbathrooms;
    }

    public void setNumberofbathrooms(Integer numberofbathrooms) {
        this.numberofbathrooms = numberofbathrooms;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getGarage() {
        return garage;
    }

    public void setGarage(Integer garage) {
        this.garage = garage;
    }

    public Integer getSurface() {
        return surface;
    }

    public void setSurface(Integer surface) {
        this.surface = surface;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
