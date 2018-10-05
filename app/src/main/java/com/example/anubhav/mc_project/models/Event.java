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
/*
    public boolean changeRequiredPeople(int quantity) {
        if (requiredPeopleCount + quantity >= 0) {
            requiredPeopleCount += quantity;

            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child(DB_REF_NAME).child(Integer.toString(this.eventID))
                    .child("Required People").setValue(requiredPeopleCount);

            return true;
        } else {
            return false;
        }
    }

    public boolean changeRequiredTeams(int quantity) {
        if (requiredTeamCount + quantity >= 0) {
            requiredTeamCount += quantity;


            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child(DB_REF_NAME).child(Integer.toString(this.eventID))
                    .child("Required Teams").setValue(requiredTeamCount);

            return true;
        } else {
            return false;
        }
    }
    */
}
