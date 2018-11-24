package com.example.anubhav.mc_project.models;

import android.location.Location;

public class EventLocation {

    public double latitude;
    public double longitude;

    public EventLocation() {

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public EventLocation(Location location) {
        this.latitude = location.getLatitude();

        this.longitude = location.getLongitude();
    }
}
