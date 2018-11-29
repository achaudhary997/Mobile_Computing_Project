package com.example.anubhav.mc_project;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.anubhav.mc_project.models.Event;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventLocationTimeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventLocationTimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventLocationTimeFragment extends Fragment {

    private static final String ARG_PARAM = "Event";

    private Event event;

    private Button showMapButton;
    private TextView eventStartTime, eventEndTime;

    private OnFragmentInteractionListener mListener;

    public EventLocationTimeFragment() {
        // Required empty public constructor
    }


    public static EventLocationTimeFragment newInstance(Bundle bundle) {
        EventLocationTimeFragment fragment = new EventLocationTimeFragment();
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

        View view = inflater.inflate(R.layout.fragment_event_location_time, container, false);
        showMapButton = view.findViewById(R.id.eventlist_show_map);
        eventStartTime = view.findViewById(R.id.event_location_start_time);
        eventEndTime = view.findViewById(R.id.event_location_end_time);

        showMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String latAndLong = event.getLocation().getLatitude() + "," + event.getLocation().getLongitude();

                Uri address = Uri.parse("geo:"+latAndLong+"?q="+latAndLong+"(Event Location)");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, address);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        String time = eventStartTime.getText().toString() + ": " + event.getStartDay() + ", " + event.getStartTime();
        eventStartTime.setText(time);
        time = eventEndTime.getText().toString() + ": " + event.getEndDay() + ", " + event.getEndTime();
        eventEndTime.setText(time);
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
        }*/
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
