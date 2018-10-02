package com.example.anubhav.mc_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Login extends AppCompatActivity{

    private Button loginButton;
    private EditText loginEmail, loginPassword;
    private TextView registerButton;
    private FirebaseAuth mAuth;
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

    private static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    private static boolean validateStrongPassword(String passwordStr) {
        Matcher matcher = VALID_PASSWORD_STRONG_REGEX.matcher(passwordStr);
        return matcher.find();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = (Button)findViewById(R.id.loginButton);
        loginEmail = (EditText)findViewById(R.id.loginEmail);
        loginPassword = (EditText)findViewById(R.id.loginPassword);
        registerButton = (TextView)findViewById(R.id.registerButton);

        mAuth = FirebaseAuth.getInstance();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startHomePageActivity();
        }
    }

    private void startHomePageActivity() {
        Intent homePage = new Intent(this, HomePage.class);
        startActivity(homePage);
    }

    private void login() {
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();
        if (validateEmail(email) && validateStrongPassword(password)) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                System.out.println("Login Succsessful");
                                startHomePageActivity();
                            } else {
                                Toast.makeText(Login.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Email/Password is in incorrect format.", Toast.LENGTH_SHORT).show();
        }
    }

    private void register() {
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();
        if (validateEmail(email) && validateStrongPassword(password)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                System.out.println("Successful"); // USE LOG.d for this
                                startHomePageActivity();
                            } else {
                                Toast.makeText(Login.this, "Registration not successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Email/Password is in incorrect format.", Toast.LENGTH_SHORT).show();
        }
    }


}
