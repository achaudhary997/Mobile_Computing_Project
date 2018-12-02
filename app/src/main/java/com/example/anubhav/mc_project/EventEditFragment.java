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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.anubhav.mc_project.models.Chat;
import com.example.anubhav.mc_project.models.Event;
import com.example.anubhav.mc_project.models.EventLocation;
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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EventEditFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM = "Event";


    private Event event;
    private OnFragmentInteractionListener mListener;

    private Bundle args;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private String selectedUserUID;

    private Button addEventButton, chooseStartDateButton, chooseStartTimeButton, chooseEndTimeButton, chooseEndDateButton;
    private EditText eventName, startTime, endTime, requiredNumber, teamSize, prizeMoney, startDate, endDate, eventLocation;
    private CheckBox teamOrIndi, gameType;
    private int mYear, mMonth, mDay, mHour, mMinute;

    LocationManager locationManager;
    EventLocation evLocation;

    public boolean checkEmptyFields() {
        if (eventName.getText().toString().equals("") || startDate.getText().toString().equals("") ||
                startTime.getText().toString().equals("") || endDate.getText().toString().equals("") ||
                endTime.getText().toString().equals("") || requiredNumber.getText().toString().equals("")) {
            return false;
        }

        if (gameType.isChecked() && (prizeMoney.getText().toString().equals(""))) {
            return false;
        }

        if (teamOrIndi.isChecked() && teamSize.getText().toString().equals("")) {
            return false;
        }
        return true;
    }



    public EventEditFragment() {
        // Required empty public constructor
    }


    public static EventEditFragment newInstance(Bundle bundle) {
        EventEditFragment fragment = new EventEditFragment();
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


        View view = inflater.inflate(R.layout.fragment_event_edit, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        selectedUserUID = mUser.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        addEventButton = view.findViewById(R.id.event_edit_addevent);
        eventName = view.findViewById(R.id.event_edit_eventname);
        startDate = view.findViewById(R.id.event_edit_eventstartdate);
        startTime = view.findViewById(R.id.event_edit_eventstarttime);
        endDate = view.findViewById(R.id.event_edit_eventenddate);
        endTime = view.findViewById(R.id.event_edit_eventendtime);
        requiredNumber = view.findViewById(R.id.event_edit_reqdpeople);
        teamSize = view.findViewById(R.id.event_edit_teamsize);
        prizeMoney = view.findViewById(R.id.event_edit_prizemoney);
        teamOrIndi = view.findViewById(R.id.event_edit_teamcheckbox);
        gameType = view.findViewById(R.id.event_edit_gametypecheckbox);
        eventLocation = view.findViewById(R.id.event_edit_location);
        chooseStartDateButton = view.findViewById(R.id.event_edit_choosestartdate);
        chooseStartTimeButton = view.findViewById(R.id.event_edit_choosestarttime);
        chooseEndDateButton = view.findViewById(R.id.event_edit_chooseenddate);
        chooseEndTimeButton = view.findViewById(R.id.event_edit_chooseendtime);

        eventName.setText(event.getEventName());
        startDate.setText(event.getStartDay());
        startTime.setText(event.getStartTime());
        endDate.setText(event.getEndDay());
        endTime.setText(event.getEndTime());
        requiredNumber.setText(event.getRequiredCount());
        eventLocation.setText(event.getLocation().toString());
        eventLocation.setEnabled(false);
        if (event.getGameType().equals("Competition")) {
            gameType.setChecked(true);
            prizeMoney.setVisibility(View.VISIBLE);
            prizeMoney.setText(event.getPrizeMoney());
        }

        if (event.getTeamEvent().equals("Team")) {
            teamOrIndi.setChecked(true);
            teamSize.setVisibility(View.VISIBLE);
            teamSize.setText(event.getTeamSize());
        }


        teamOrIndi.setOnClickListener(this);
        gameType.setOnClickListener(this);
        addEventButton.setOnClickListener(this);
        chooseStartDateButton.setOnClickListener(this);
        chooseStartTimeButton.setOnClickListener(this);
        chooseEndDateButton.setOnClickListener(this);
        chooseEndTimeButton.setOnClickListener(this);



        return view;
    }

    @Override
    public void onClick(final View v) {
        if (v == teamOrIndi) {
            if (teamOrIndi.isChecked()) {
                teamSize.setVisibility(View.VISIBLE);
            } else {
                teamSize.setVisibility(View.GONE);
                teamSize.setText("None");
            }
        } else if (v == gameType) {
            if (gameType.isChecked()) {
                prizeMoney.setVisibility(View.VISIBLE);
            } else {
                prizeMoney.setText("0");
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



            if (checkEmptyFields()) {
                addEvent(eventName.getText().toString(), startTime.getText().toString(),
                        endTime.getText().toString(), game,
                        requiredNumber.getText().toString(), teamSize.getText().toString(),
                        prizeMoney.getText().toString(), team, startDate.getText().toString(), endDate.getText().toString(),
                        eventLocation.toString());
            } else {
                Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
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
        }
    }


    public void addEvent(final String eventName, final String startTime, final String endTime,
                         final String gameType, final String requiredNumber, final String teamSize,
                         final String prizeMoney, final String teamOrIndi, final String startDate,
                         final String endDate, final String eventLocation) {


        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                event.setAll(event.getEventID(), eventName, startTime, endTime, teamOrIndi, requiredNumber,
                        teamSize, gameType, prizeMoney, selectedUserUID, startDate, endDate, evLocation, event.getRegisteredUsers());
                mDatabaseReference.child(Helper.eventNode).child(event.getEventID()).setValue(event);
                Log.d("Firebase", "onDataChange");
                Toast.makeText(getActivity(), "Event Modified and published", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase", "onCancelled");
            }
        });



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
