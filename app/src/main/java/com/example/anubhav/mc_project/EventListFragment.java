package com.example.anubhav.mc_project;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anubhav.mc_project.models.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventListFragment extends Fragment {
    private RecyclerView eventRecyclerView;
    private EventAdapter eAdapter;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseReference;
    private String userID;
    
    public EventListFragment() {
        
    }
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_home_page, container, false);
        eventRecyclerView = view.findViewById(R.id.home_page_recycler_view);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private void updateUI() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        userID = user.getUid();



        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<Event> events = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap: dataSnapshot.child(Helper.eventNode).getChildren()) {
                    Log.d("Reading message", "onDataChange: ");
                    //GenericTypeIndicator<Event> t = new GenericTypeIndicator<Event>(){};
                    System.out.println("Reading message" + snap.getValue());
                    events.add(snap.getValue(Event.class));
                }
                eAdapter = new EventAdapter(events);
                eventRecyclerView.setAdapter(eAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    
    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Event event;
        private TextView eventName, eventStartTime, eventEndTime;

        public EventHolder (LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.event_list_item_home_page, parent, false));
            eventName = itemView.findViewById(R.id.eventlist_event_name);
            eventStartTime = itemView.findViewById(R.id.eventlist_start_time);
            eventEndTime = itemView.findViewById(R.id.eventlist_end_time);
        }

        public void bind (Event event) {
            this.event = event;
            eventName.setText(event.getEventName());
            eventStartTime.setText(event.getStartTime());
            eventEndTime.setText(event.getEndTime());
        }

        @Override
        public void onClick(View view) {
            // Create intent here and show new activity/fragment
        }
    }

    private class EventAdapter extends RecyclerView.Adapter<EventHolder> {
        private ArrayList<Event> eventList;

        public EventAdapter(ArrayList<Event> events) {
            eventList = events;
        }

        @Override
        public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new EventHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(EventHolder holder, int position) {
            Event event = eventList.get(position);
            holder.bind(event);
        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }
    }


}
