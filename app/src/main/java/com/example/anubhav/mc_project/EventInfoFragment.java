package com.example.anubhav.mc_project;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anubhav.mc_project.models.Chat;
import com.example.anubhav.mc_project.models.Event;
import com.example.anubhav.mc_project.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventInfoFragment extends Fragment {

    private static final String ARG_PARAM = "Event";


    private Event event;
    private OnFragmentInteractionListener mListener;

    private Bundle args;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private String username;
    private Chat sessionChat = new Chat();
    private String selectedUserUID;

    private boolean nodeFound = false;
    private String foundNodeKey;

    private TextView eventName, eventCreator, eventType, eventPrize, eventRequired, eventTeamOrIndi;

    private Button messageButton, joinButton;

    private ArrayList<Message> messages = sessionChat.getMessages();


    public EventInfoFragment() {
        // Required empty public constructor
    }


    public static EventInfoFragment newInstance(Bundle bundle) {
        EventInfoFragment fragment = new EventInfoFragment();
        Bundle args = new Bundle();
        args.putBundle(ARG_PARAM, bundle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = (Event) getArguments().getBundle(ARG_PARAM).getSerializable(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_event_info, container, false);
        eventName = view.findViewById(R.id.event_info_event_name);
        eventCreator = view.findViewById(R.id.event_info_event_creator);
        eventType = view.findViewById(R.id.event_info_event_type);
        eventPrize = view.findViewById(R.id.event_info_event_prize_money);
        eventRequired = view.findViewById(R.id.event_info_event_required_members);
        eventTeamOrIndi = view.findViewById(R.id.event_info_event_team_or_indi);
        messageButton = view.findViewById(R.id.event_info_message_creator);
        joinButton = view.findViewById(R.id.event_info_join_button);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        selectedUserUID = mUser.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        if (event.getCreator().equals(selectedUserUID)) {
            joinButton.setEnabled(false);
            messageButton.setEnabled(false);
        }

        try {
            Query query = mDatabaseReference.child(Helper.eventNode).child(event.getEventID()).child("Registered Users").child(selectedUserUID);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        joinButton.setText("Leave Event");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch(Exception e) {
            Log.d("Error", "Error in Firebase query");
        }



        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String eventID = event.getEventID();

                        mDatabaseReference.child(Helper.eventNode).child(eventID).child("registeredUsers").child(selectedUserUID).setValue(true);
                        Log.d("Firebase", "onDataChange");
                        Toast.makeText(getActivity(), "Joined Event", Toast.LENGTH_SHORT).show();
                        joinButton.setText("Leave Event");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("Firebase", "onCancelled");
                    }
                });

            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser();

                final String creatorID = event.getCreator();

                Intent messageIntent = new Intent(getActivity(), ChatActivity.class);
                messageIntent.putExtra("event_creator_id", creatorID);
                startActivity(messageIntent);
            }
        });

        String text = "Event Name: " + event.getEventName();
        eventName.setText(text);
        text = "Created By: " + event.getCreator();
        eventCreator.setText(text);
        text = "Event Type: ";
        switch (event.getGameType()) {
            case "Competition":
                text += "Competition";
                break;
            case "Friendly":
                text += "Friendly";
                break;
        }
        eventType.setText(text);

        text = "Prize Money: " + event.getPrizeMoney();
        eventPrize.setText(text);

        text = "";
        switch (event.getTeamEvent()) {
            case "Individual":
                text += "Individual event.";
                break;
            case "Team":
                text += "Team event with " + event.getTeamSize() + " members per team";
                break;
        }
        eventTeamOrIndi.setText(text);
        text = event.getRequiredCount() + " more members/teams required for this event";
        eventRequired.setText(text);
        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
