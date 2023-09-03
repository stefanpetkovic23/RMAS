package com.example.dealspotter.Models;

public class Favorites {
    private String propertyKey;
    private boolean isFavorite;

    public Favorites() {
        // Default constructor for Firebase
    }

    public Favorites(String propertyKey, boolean isFavorite) {
        this.propertyKey = propertyKey;
        this.isFavorite = isFavorite;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
