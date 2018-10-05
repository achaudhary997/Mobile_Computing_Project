package com.example.anubhav.mc_project.models;

import android.content.Context;

import com.example.anubhav.mc_project.Helper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EventList {
    private ArrayList<Event> events = new ArrayList<>();
    private static EventList eventList;

    private EventList(){;}

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseReference;
    private String userID;

    public static EventList get(Context context) {
        if (eventList == null) {
            eventList = new EventList();
        }
        return eventList;
    }

    public void addEvent(String eventName, String startTime, String endTime, String gameType,
                         String requiredNumber, String teamSize, String prizeMoney,
                         String teamOrIndi) {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        userID = user.getUid();


        String eventID = Integer.toString(events.size() + 1);

        Event event = new Event(eventID, eventName, startTime, endTime, teamOrIndi, requiredNumber,
                teamSize, gameType, prizeMoney, userID);

        events.add(event);
        mDatabaseReference.child(Helper.eventNode).child(eventID).setValue(event);

    }

    public ArrayList<Event> getEvents() {
        return events;
    }
}
