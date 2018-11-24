package com.example.anubhav.mc_project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.anubhav.mc_project.models.Event;
import com.example.anubhav.mc_project.models.EventLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


/*
 * Fragment class to add events to Firebase
 */

public class EventFragment extends Fragment implements View.OnClickListener, LocationListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Button addEventButton, chooseStartDateButton, chooseStartTimeButton, chooseEndTimeButton, chooseEndDateButton, chooseLocationButton;
    private EditText eventName, startTime, endTime, requiredNumber, teamSize, prizeMoney, startDate, endDate, eventLocation;
    private CheckBox teamOrIndi, gameType;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseReference;
    private String userID;


    LocationManager locationManager;
    EventLocation evLocation;

    public EventFragment() {
        // Required empty public constructor
    }

    public static EventFragment newInstance(String param1, String param2) {
        EventFragment fragment = new EventFragment();
        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        addEventButton = view.findViewById(R.id.event_addevent);
        eventName = view.findViewById(R.id.event_eventname);
        startDate = view.findViewById(R.id.event_eventstartdate);
        startTime = view.findViewById(R.id.event_eventstarttime);
        endDate = view.findViewById(R.id.event_eventenddate);
        endTime = view.findViewById(R.id.event_eventendtime);
        requiredNumber = view.findViewById(R.id.event_reqdpeople);
        teamSize = view.findViewById(R.id.event_teamsize);
        prizeMoney = view.findViewById(R.id.event_prizemoney);
        teamOrIndi = view.findViewById(R.id.event_teamcheckbox);
        gameType = view.findViewById(R.id.event_gametypecheckbox);
        eventLocation = view.findViewById(R.id.event_location);
        chooseStartDateButton = view.findViewById(R.id.event_choosestartdate);
        chooseStartTimeButton = view.findViewById(R.id.event_choosestarttime);
        chooseEndDateButton = view.findViewById(R.id.event_chooseenddate);
        chooseEndTimeButton = view.findViewById(R.id.event_chooseendtime);
        chooseLocationButton = view.findViewById(R.id.event_location_button);

        teamOrIndi.setOnClickListener(this);
        gameType.setOnClickListener(this);
        addEventButton.setOnClickListener(this);
        chooseStartDateButton.setOnClickListener(this);
        chooseStartTimeButton.setOnClickListener(this);
        chooseEndDateButton.setOnClickListener(this);
        chooseEndTimeButton.setOnClickListener(this);
        chooseLocationButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(final View v) {
        if (v == teamOrIndi) {
            if (teamOrIndi.isChecked()) {
                teamSize.setVisibility(View.VISIBLE);
            } else {
                teamSize.setVisibility(View.GONE);
            }
        } else if (v == gameType) {
            if (gameType.isChecked()) {
                prizeMoney.setVisibility(View.VISIBLE);
            } else {
                prizeMoney.setVisibility(View.GONE);
            }
        } else if (v == addEventButton) {

            String game, team;
            if (gameType.isChecked()) {
                game = "Competition";
            } else game = "Friendly";
            if (teamOrIndi.isChecked()) {
                team = "Team";
            } else team = "Individual";

            addEvent(eventName.getText().toString(), startTime.getText().toString(),
                    endTime.getText().toString(), game,
                    requiredNumber.getText().toString(), teamSize.getText().toString(),
                    prizeMoney.getText().toString(), team, startDate.getText().toString(), endDate.getText().toString(),
                    eventLocation.toString());

        } else if (v == chooseStartDateButton || v == chooseEndDateButton) {
            Log.d("Button:", "Inside Date");
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    if (v == chooseStartDateButton) {
                        startDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    } else {
                        endDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();

        } else if (v == chooseStartTimeButton || v == chooseEndTimeButton) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if (v == chooseStartTimeButton) {
                        startTime.setText(hourOfDay + ":" + minute);
                    } else {
                        endTime.setText(hourOfDay + ":" + minute);
                    }

                }
            }, mHour, mMinute, false);
            timePickerDialog.show();
        } else if (v == chooseLocationButton) {
            Log.d("Button:", "Inside location listener");
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

            }
            getLocation();
        }
    }


    /*
     * Functions to get the current Location from the GPS provider
     * AndroStock.com
     */

    private void getLocation() {
        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        eventLocation.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
        evLocation = new EventLocation(location);
        System.out.println("Location:" + " " + location);
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            eventLocation.setText(eventLocation.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
        } catch(Exception e) {

        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getActivity(), "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    /*
     * Function to add the event to firebase
     */


    public void addEvent(final String eventName, final String startTime, final String endTime,
                         final String gameType, final String requiredNumber, final String teamSize,
                         final String prizeMoney, final String teamOrIndi, final String startDate,
                         final String endDate, final String eventLocation) {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        userID = user.getUid();

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String eventID = Long.toString(dataSnapshot.child(Helper.eventNode).getChildrenCount() + 1);
                Event event = new Event(eventID, eventName, startTime, endTime, teamOrIndi, requiredNumber,
                        teamSize, gameType, prizeMoney, userID, startDate, endDate, evLocation);
                mDatabaseReference.child(Helper.eventNode).child(eventID).setValue(event);
                Log.d("Firebase", "onDataChange");
                Toast.makeText(getActivity(), "Event Created and published", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase", "onCancelled");
            }
        });



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

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
