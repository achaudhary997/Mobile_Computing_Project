package com.example.anubhav.mc_project;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

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
