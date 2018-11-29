package com.example.anubhav.mc_project;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anubhav.mc_project.models.Event;

public class EventInfoFragment extends Fragment {

    private static final String ARG_PARAM = "Event";


    private Event event;
    private OnFragmentInteractionListener mListener;

    private Bundle args;

    private TextView eventName, eventCreator, eventType, eventPrize, eventRequired, eventTeamOrIndi;


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
