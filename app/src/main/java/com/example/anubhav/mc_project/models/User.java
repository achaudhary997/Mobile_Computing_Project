package com.example.anubhav.mc_project.models;

import java.util.ArrayList;

public class User {
    // We will be using Firebase User unless required otherwise.
    private String uid;
    private String fullName;
    private String email;
    private String imageUri;
    private double rating;
    private String phoneNumber;
    private ArrayList<String> interests;
    private int eventsConducted;
    private int distanceThreshold;

    public int getDistanceThreshold() {
        return distanceThreshold;
    }

    public void setDistanceThreshold(int distanceThreshold) {
        this.distanceThreshold = distanceThreshold;
    }
    // private List<Events> // Uncomment and implement

    public User() {}

    public User(String uid, String fullName, String email, double rating, String phoneNumber) {
        this.uid = uid;
        this.fullName = fullName;
        this.email = email;
        this.rating = rating;
        this.phoneNumber = phoneNumber;
        this.eventsConducted = 0;

    }

    public int getEventsConducted() {
        return eventsConducted;
    }

    public void setEventsConducted(int eventsConducted) {
        this.eventsConducted = eventsConducted;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }
}
