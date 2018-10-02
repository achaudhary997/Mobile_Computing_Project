package com.example.anubhav.mc_project;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anubhav.mc_project.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private User currentUser;

    public ProfileFragment() {}

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void getUserDetails(final String uid, final ProgressDialog pd, final View view) {
        FirebaseDatabase.getInstance().getReference(Helper.userNode)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean found=false;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        if (user.getUid().equals(uid)) {
                            currentUser = user;
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        pd.dismiss();
                        displayData(view);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
    }

    public void displayData(View view) {
        TextView fullName = view.findViewById(R.id.profile_full_name);
        TextView email = view.findViewById(R.id.profile_email);
        TextView contact = view.findViewById(R.id.profile_phone_number);

        fullName.setText(currentUser.getFullName());
        email.setText(currentUser.getEmail());
        contact.setText(currentUser.getPhoneNumber());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Fetching Profile Details...");
        String userId = getArguments().getString("user_id");
        pd.show();
        getUserDetails(userId, pd, view);
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
