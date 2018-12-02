package com.example.anubhav.mc_project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anubhav.mc_project.models.Event;
import com.example.anubhav.mc_project.models.EventLocation;
import com.google.android.gms.maps.MapView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EventListFragment extends Fragment {
    public final int distance_threshold = 3; //in km

    private RecyclerView eventRecyclerView;
    private EventAdapter eAdapter;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseReference;
    private String userID;

    public ProgressBar progressBar;


    
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
        progressBar = view.findViewById(R.id.event_load_progressbar);
        progressBar.setMax(100);
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
    
    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Event event;
        private TextView eventName;


        public EventHolder (LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.event_list_item_home_page, parent, false));
            eventName = itemView.findViewById(R.id.eventlist_event_name);
            itemView.setOnClickListener(this);

        }

        public void bind (Event event) {
            this.event = event;
            eventName.setText(event.getEventName());

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
            //progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
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
        private double curLatitude=0.0, curLongitude=0.0;
        private GPSlocation gps;
        HashMap<String, Boolean> users;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            gps = new GPSlocation();
            gps.getLocation();
            //curLatitude = gps.getLatitude();
            //curLongitude = gps.getLongitude();
        }

        @Override
        protected void onPostExecute(ArrayList<Event> events) {
            super.onPostExecute(events);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date curDate = new Date();
            Date eventDate;
            String dateString = formatter.format(curDate);
            Boolean isAttending = false, eventCompleted = false;
            //final int userEventsConducted = 0;

            try {
                curDate = formatter.parse(dateString);
            } catch (Exception e) {
                e.printStackTrace();
            }



            for (final Event event : events) {
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
                        Map.Entry pair = (Map.Entry)it.next();
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
                    eventDate = formatter.parse(event.getEndDay() + " "  + event.getEndTime());

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
                                                    userEventsConducted = (Long)dataSnapshot.child("eventsConducted").getValue();
                                                    userCurrentRating = (Double)dataSnapshot.child("rating").getValue();
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
            progressBar.setVisibility(View.GONE);
            eAdapter.setEventList(this.eventList);

            eventRecyclerView.setAdapter(eAdapter);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected ArrayList<Event> doInBackground(ArrayList<Event>... events) {
            while (this.curLatitude == 0.0) {
                publishProgress(0);
            }
            publishProgress(100);
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

        class GPSlocation{
            LocationManager locationManager;
            double latitude, longitude;
            public void getLocation() {
                try {
                    locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsSensor);
                }
                catch(SecurityException e) {
                    e.printStackTrace();
                }
            }

            public double getLatitude() {
                return latitude;
            }

            public double getLongitude() {
                return longitude;
            }

            LocationListener gpsSensor = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    curLatitude = latitude;
                    curLongitude = longitude;
                    locationManager.removeUpdates(gpsSensor);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {
                    Toast.makeText(getActivity(), "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
                }
            };

        }


    }




}
