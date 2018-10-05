package com.example.anubhav.mc_project;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.ImageView;

import com.example.anubhav.mc_project.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    public static String userNode = "users";
    public static String eventNode = "events";
    public static String logTag = "sports_buddy_log";

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * ^                 # start-of-string
     *  (?=.*[0-9])       # a digit must occur at least once
     *  (?=.*[a-z])       # a lower case letter must occur at least once
     *  (?=.*[A-Z])       # an upper case letter must occur at least once
     *  (?=.*[@#$%^&+=])  # a special character must occur at least once
     *  (?=\S+$)          # no whitespace allowed in the entire string
     *  .{8,}             # anything, at least eight places though
     *  $                 # end-of-string
     */
    private static final Pattern VALID_PASSWORD_STRONG_REGEX =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    public static boolean validateStrongPassword(String passwordStr) {
        Matcher matcher = VALID_PASSWORD_STRONG_REGEX.matcher(passwordStr);
        return matcher.find();
    }

}
