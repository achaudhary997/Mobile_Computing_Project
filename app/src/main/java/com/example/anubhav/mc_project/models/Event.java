package com.example.anubhav.mc_project.models;

import android.location.Location;

import com.google.firebase.database.DatabaseReference;

import java.util.Date;

public class Event {

    public static final String DB_REF_NAME = "Events";

    /*
     Class Attributes
     */
    private String eventID;
    private String eventName;
    private String startTime;
    private String endTime;
    private String teamEvent;
    private String requiredCount;
    private String teamSize;
    private String gameType; //Friendly / Competition
    private String prizeMoney;
    private String creator;
    private Location location;


    public Event() {

    }

    public Event(String eventID, String eventName, String startTime, String endTime, String teamEvent,
                 String requiredCount, String teamSize, String gameType,
                 String prizeMoney, String creator) {
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
        //this.location = location;
    }

    public String getEventID() {
        return eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public String getStartTime() {
        return startTime;
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

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setEventID(String eventID) {

        this.eventID = eventID;
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

    public Location getLocation() {
        return location;
    }

}
