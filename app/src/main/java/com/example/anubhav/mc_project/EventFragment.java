package com.example.anubhav.mc_project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.anubhav.mc_project.models.EventList;

import java.util.Calendar;


public class EventFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Button addEventButton, chooseStartDateButton, chooseStartTimeButton, chooseEndTimeButton, chooseEndDateButton;
    private EditText eventName, startTime, endTime, requiredNumber, teamSize, prizeMoney, startDate, endDate;
    private CheckBox teamOrIndi, gameType;
    private int mYear, mMonth, mDay, mHour, mMinute;

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
        chooseStartDateButton = view.findViewById(R.id.event_choosestartdate);
        chooseStartTimeButton = view.findViewById(R.id.event_choosestarttime);
        chooseEndDateButton = view.findViewById(R.id.event_chooseenddate);
        chooseEndTimeButton = view.findViewById(R.id.event_chooseendtime);

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
            }
        } else if (v == gameType) {
            if (gameType.isChecked()) {
                prizeMoney.setVisibility(View.VISIBLE);
            } else {
                prizeMoney.setVisibility(View.GONE);
            }
        } else if (v == addEventButton) {
            EventList eventList = EventList.get(getActivity());

            String game, team;
            if (gameType.isChecked()) {
                game = "Competition";
            } else game = "Friendly";
            if (teamOrIndi.isChecked()) {
                team = "Team";
            } else team = "Individual";

            eventList.addEvent(eventName.getText().toString(), startTime.getText().toString(),
                    endTime.getText().toString(), game,
                    requiredNumber.getText().toString(), teamSize.getText().toString(),
                    prizeMoney.getText().toString(), team);
        } else if (v == chooseStartDateButton || v == chooseEndDateButton) {
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

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
