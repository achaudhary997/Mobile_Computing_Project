package com.example.anubhav.mc_project.models;

import android.location.Location;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class Event implements Serializable {

    public static final String DB_REF_NAME = "Events";

    /*
     Class Attributes
     */
    private String eventID;
    private String eventName;
    private String startTime;
    private String endTime;
    private String startDay;
    private String endDay;
    private String teamEvent;
    private String requiredCount;
    private String teamSize;
    private String gameType;
    private String prizeMoney;
    private String creator;
    private EventLocation location;
    private HashMap<String, Boolean> registeredUsers;
    private double creatorRating;


    public HashMap<String, Boolean> getRegisteredUsers() {
        return registeredUsers;
    }

    public void setRegisteredUsers(HashMap<String, Boolean> registeredUsers) {
        this.registeredUsers = registeredUsers;
    }

    public Event() {

    }


    public Event(String eventID, String eventName, String startTime, String endTime, String teamEvent,
                 String requiredCount, String teamSize, String gameType,
                 String prizeMoney, String creator, String startDay, String endDay, EventLocation location, double creatorRating) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.teamEvent = teamEvent;
        this.requiredCount = requiredCount;
        this.teamSize = teamSize;
        this.gameType = gameType;
        this.prizeMoney = prizeMoney;
        this.creator = creator;
        this.startDay = startDay;
        this.endDay = endDay;
        this.location = location;
        this.registeredUsers = new HashMap<>();
        this.creatorRating = creatorRating;
    }

    public void setAll(String eventID, String eventName, String startTime, String endTime, String teamEvent,
                       String requiredCount, String teamSize, String gameType, String prizeMoney,
                       String creator, String startDay, String endDay, EventLocation location,
                       HashMap<String, Boolean> registeredUsers, double creatorRating) {
            this.eventID = eventID;
            this.eventName = eventName;
            this.startTime = startTime;
            this.endTime = endTime;
            this.teamEvent = teamEvent;
            this.requiredCount = requiredCount;
            this.teamSize = teamSize;
            this.gameType = gameType;
            this.prizeMoney = prizeMoney;
            this.creator = creator;
            this.startDay = startDay;
            this.endDay = endDay;
            this.location = location;
            this.registeredUsers = registeredUsers;
            this.creatorRating = creatorRating;
    }

    public String getEventID() {
        return eventID;
    }

    public String getStartDay() {
        return startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public String getCreator() {
        return creator;
    }

    public void setStartDay(String startDay) {

        this.startDay = startDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public String getEventName() {
        return eventName;
    }

    public String getStartTime() {
        return startTime;
    }

    public double getCreatorRating() {
        return creatorRating;
    }

    public void setCreatorRating(double creatorRating) {
        this.creatorRating = creatorRating;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setTeamEvent(String teamEvent) {
        this.teamEvent = teamEvent;
    }

    public void setRequiredCount(String requiredCount) {
        this.requiredCount = requiredCount;
    }

    public void setTeamSize(String teamSize) {
        this.teamSize = teamSize;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public void setPrizeMoney(String prizeMoney) {
        this.prizeMoney = prizeMoney;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setLocation(EventLocation location) {
        this.location = location;
    }

    public void setEventID(String eventID) {

        this.eventID = eventID;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventID='" + eventID + '\'' +
                ", eventName='" + eventName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", startDay='" + startDay + '\'' +
                ", endDay='" + endDay + '\'' +
                ", teamEvent='" + teamEvent + '\'' +
                ", requiredCount='" + requiredCount + '\'' +
                ", teamSize='" + teamSize + '\'' +
                ", gameType='" + gameType + '\'' +
                ", prizeMoney='" + prizeMoney + '\'' +
                ", creator='" + creator + '\'' +
                ", location=" + location +
                ", registeredUsers=" + registeredUsers +
                ", creatorRating=" + creatorRating +
                '}';
    }

    public String getTeamEvent() {
        return teamEvent;
    }

    public String getRequiredCount() {
        return requiredCount;
    }

    public String getTeamSize() {
        return teamSize;
    }

    public String getGameType() {
        return gameType;
    }

    public String getPrizeMoney() {
        return prizeMoney;
    }

    public EventLocation getLocation() {
        return location;
    }


}

