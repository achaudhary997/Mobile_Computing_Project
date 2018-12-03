package com.example.anubhav.mc_project;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.anubhav.mc_project.models.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import java.util.Map;

public class EventListFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener {
    public final int distance_threshold = 5; //in km

    private RecyclerView eventRecyclerView;
    private EventAdapter eAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseReference;
    private String userID;
    private boolean refreshUI = false;

    public ProgressDialog progressBar;

    HashMap<String, ArrayList<String>> userInterestMap = new HashMap<>();

    public double curLatitude = 0.0;
    public double curLongitude = 0.0;
    
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

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        onRefresh();


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        curLatitude = data.getDoubleExtra("Latitude", 0.0);
        curLongitude = data.getDoubleExtra("Longitude", 0.0);
        Log.d("Received coordinates:", Double.toString(curLatitude) + " "  + Double.toString(curLongitude));
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
//                    System.out.println("Reading message" + snap.getValue());
                    Event event = snap.getValue(Event.class);
                    events.add(event);
                    ArrayList<String> interests = (ArrayList<String>) dataSnapshot.child(Helper.userNode).child(event.getCreator()).child("interests").getValue();
                    userInterestMap.put(event.getCreator(), interests);
                }
                if (!userInterestMap.containsKey(userID)) {
                    ArrayList<String> interests = (ArrayList<String>) dataSnapshot.child(Helper.userNode).child(userID).child("interests").getValue();
                    userInterestMap.put(userID, interests);
                }
                //if (eAdapter == null) {
                eAdapter = new EventAdapter(events);
                    //eventRecyclerView.setAdapter(eAdapter);
                //} else {
                //   eAdapter.notifyDataSetChanged();
                //}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        progressBar = new ProgressDialog(getActivity());
        progressBar.setMessage("Getting the best events for you...");
        progressBar.show();
        Intent intent = new Intent(getActivity().getBaseContext(), LocationActivity.class);
        startActivityForResult(intent, 1);
        updateUI();
    }

    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Event event;
        private TextView eventName;
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();

        public EventHolder (LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.event_list_item_home_page, parent, false));
            eventName = itemView.findViewById(R.id.eventlist_event_name);

            itemView.setOnClickListener(this);

        }

        public void bind (Event event) {
            this.event = event;
            eventName.setText(event.getEventName());
            if (event.getCreator().equals(uid)) {
                RelativeLayout layout = itemView.findViewById(R.id.list_item_rel_layout);
                layout.setBackgroundColor(Color.parseColor("#6C2323"));
            }

        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity().getBaseContext(), EventDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Event", this.event);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        }
    }

    private class EventAdapter extends RecyclerView.Adapter<EventHolder> {
        private ArrayList<Event> eventList = null;

        public void setEventList(ArrayList<Event> eventList) {
            this.eventList = eventList;
        }

        public EventAdapter(ArrayList<Event> events) {

            new SetupEventsDisplay().execute(events);

        }



        @Override
        public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new EventHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(EventHolder holder, int position) {
            final Event event = eventList.get(position);
            holder.bind(event);

        }

        @Override
        public int getItemCount() {
            if (eventList == null) {
                return 0;
            }
            return eventList.size();
        }
    }

    private class SetupEventsDisplay extends AsyncTask<ArrayList<Event>, Integer, ArrayList<Event>> {

        private ArrayList<Event> eventList = new ArrayList<>();
        //private double curLatitude = 0.0, curLongitude = 0.0;

        HashMap<String, Boolean> users;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Get gps data
            //((Activity)getContext()).startActivityForResult(new Intent(, LocationActivity.class));

        }


        @Override
        protected void onPostExecute(ArrayList<Event> events) {
            super.onPostExecute(events);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date curDate = new Date();
            Date eventDate;
            String dateString = formatter.format(curDate);

            //final int userEventsConducted = 0;

            try {
                curDate = formatter.parse(dateString);
            } catch (Exception e) {
                e.printStackTrace();
            }


            for (final Event event : events) {
                Boolean isAttending = false, eventCompleted = false;
                Log.d("event:", event.toString());
                double eventLatitude = event.getLocation().getLatitude();
                double eventLongitude = event.getLocation().getLongitude();
                double distance_betw = distance(curLatitude, curLongitude, eventLatitude, eventLongitude, 'K');
                Log.d("Distance:", Double.toString(distance_betw));
                Log.d("Lat Long: ", Double.toString(curLatitude) + " " + Double.toString(curLongitude));
                Log.d("Lat Long2: ", Double.toString(eventLatitude) + " " + Double.toString(eventLongitude));


                //users = new HashMap<>();
                users = event.getRegisteredUsers();

                if (users != null) {
                    Log.d("users, ", users.toString());
                    Iterator it = users.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        Log.d("key in hashmap", pair.getKey().toString());
                        if (userID.equals(pair.getKey().toString())) {
                            // User is attending this event. Have to define comparator for sorting based on distance, attending and rating
                            isAttending = true;
                        }
                    }
                }

                Log.d("isattending", Boolean.toString(isAttending));


                eventDate = new Date();
                try {
                    eventDate = formatter.parse(event.getEndDay() + " " + event.getEndTime());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (curDate.compareTo(eventDate) > 0) {
                    // Event Over
                    eventCompleted = true;


                    if (isAttending) {
                        // ask to enter rating
                        AlertDialog.Builder popDialog = new AlertDialog.Builder(getActivity());
                        final RatingBar rating = new RatingBar(getActivity());
                        rating.setMax(6);
                        popDialog.setIcon(android.R.drawable.btn_star_big_on);
                        popDialog.setTitle("Please rate your previous experience at " + event.getEventName());
                        popDialog.setView(rating);

                        popDialog.setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mDatabaseReference.child(Helper.userNode).child(event.getCreator()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            long userEventsConducted = 0;
                                            double userCurrentRating = 0, new_rating = 0;

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    userEventsConducted = (Long) dataSnapshot.child("eventsConducted").getValue();
                                                    userCurrentRating = (Double) dataSnapshot.child("rating").getValue();
                                                }
                                                userEventsConducted++;
                                                new_rating = (userCurrentRating + rating.getRating()) / userEventsConducted;
                                                mDatabaseReference.child(Helper.userNode).child(event.getCreator()).child("rating").setValue(new_rating);
                                                mDatabaseReference.child(Helper.userNode).child(event.getCreator()).child("eventsConducted").setValue(userEventsConducted);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                        dialog.dismiss();
                                    }
                                });

                        popDialog.create();
                        popDialog.show();
                    }


                    // Remove from firebase
                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String eventID = event.getEventID();

                            mDatabaseReference.child(Helper.eventNode).child(eventID).removeValue();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                if (distance_betw <= distance_threshold && !eventCompleted) {
                    this.eventList.add(event);
                }


            }

            Log.d("onPostExecute ", "done");

            Collections.sort(eventList, new SortEvents());
            eAdapter.setEventList(this.eventList);
            eventRecyclerView.setAdapter(eAdapter);
            mSwipeRefreshLayout.setRefreshing(false);
            progressBar.dismiss();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected ArrayList<Event> doInBackground(ArrayList<Event>... events) {


            while (curLatitude == 0.0) {
                //    publishProgress(0);
            }

            return events[0];
        }

        private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
            double theta = lon1 - lon2;
            double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            if (unit == 'K') {
                dist = dist * 1.609344;
            } else if (unit == 'N') {
                dist = dist * 0.8684;
            }
            return (dist);
        }

        private double deg2rad(double deg) {
            return (deg * Math.PI / 180.0);
        }

        private double rad2deg(double rad) {
            return (rad * 180.0 / Math.PI);
        }
    }

    class SortEvents implements Comparator<Event> {

        @Override
        public int compare(Event a, Event b) {
            String userA = a.getCreator();
            String userB = b.getCreator();

            double ratingWeight = 0.5;
            double interestsWeight = 0.5;

            int aInterestCount = 0;
            int bInterestCount = 0;

            if (userInterestMap.containsKey(userA) && userInterestMap.containsKey(userB) && userInterestMap.containsKey(userID)) {
                ArrayList<String> currentUserInterests = userInterestMap.get(userID);
                ArrayList<String> AUserInterests = userInterestMap.get(userA);
                ArrayList<String> BUserInterests = userInterestMap.get(userB);

                for (String interest : AUserInterests) {
                    if (currentUserInterests.contains(interest)) {
                        aInterestCount += 1;
                    }
                }

                for (String interest : BUserInterests) {
                    if (currentUserInterests.contains(interest)) {
                        bInterestCount += 1;
                    }
                }
            }

            double aWeight = ratingWeight*a.getCreatorRating() + interestsWeight*aInterestCount;
            double bWeight = ratingWeight*b.getCreatorRating() + interestsWeight*bInterestCount;

            if (aWeight < bWeight) {
                return 1;
            } else {
                return -1;
            }
        }
    }



}
