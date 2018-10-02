package com.example.anubhav.mc_project.datamodels;

import android.location.Location;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class Event {

    public static final String DB_REF_NAME = "Events";

    /*
     Class Attributes
     */
    private int eventID;
    private String eventName;
    private Date startTime;
    private Date endTime;
    private boolean teamEvent;
    private int requiredPeopleCount;
    private int requiredTeamCount;
    private int teamSize;
    private String gameType; //Friendly / Competition
    private float prizeMoney;
    private Location location;

    /*
     Database Initialization
     */
    private DatabaseReference mDatabase;

    public Event(int eventID, String eventName, Date startTime, Date endTime, boolean teamEvent,
                 int requiredPeopleCount, int requiredTeamCount, int teamSize, String gameType,
                 float prizeMoney, Location location) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.teamEvent = teamEvent;
        this.requiredPeopleCount = requiredPeopleCount;
        this.requiredTeamCount = requiredTeamCount;
        this.teamSize = teamSize;
        this.gameType = gameType;
        this.prizeMoney = prizeMoney;
        this.location = location;
    }

    public int getEventID() {
        return eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public boolean isTeamEvent() {
        return teamEvent;
    }

    public int getRequiredPeopleCount() {
        return requiredPeopleCount;
    }

    public int getRequiredTeamCount() {
        return requiredTeamCount;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public String getGameType() {
        return gameType;
    }

    public float getPrizeMoney() {
        return prizeMoney;
    }

    public Location getLocation() {
        return location;
    }

    public boolean changeRequiredPeople(int quantity) {
        if (requiredPeopleCount + quantity >= 0) {
            requiredPeopleCount += quantity;
            /*
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child(DB_REF_NAME).child(Integer.toString(this.eventID))
                    .child("Required People").setValue(requiredPeopleCount);
            */
            return true;
        } else {
            return false;
        }
    }

    public boolean changeRequiredTeams(int quantity) {
        if (requiredTeamCount + quantity >= 0) {
            requiredTeamCount += quantity;

            /*
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child(DB_REF_NAME).child(Integer.toString(this.eventID))
                    .child("Required Teams").setValue(requiredTeamCount);
            */
            return true;
        } else {
            return false;
        }
    }
}
