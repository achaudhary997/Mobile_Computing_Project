package com.example.anubhav.mc_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.anubhav.mc_project.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private EditText fullName;
    private EditText emailAddress;
    private EditText contactNumber;
    private EditText password;

    private Spinner interest1;
    private Spinner interest2;
    private Spinner interest3;

    private Button registerButton;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseReference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullName = findViewById(R.id.et_full_name);
        emailAddress = findViewById(R.id.et_email);
        contactNumber = findViewById(R.id.et_contact);
        password = findViewById(R.id.et_password);

        interest1 = findViewById(R.id.spinner_interest_1);
        interest2 = findViewById(R.id.spinner_interest_2);
        interest3 = findViewById(R.id.spinner_interest_3);

        registerButton = findViewById(R.id.register_button);

        mAuth = FirebaseAuth.getInstance();

//        POPULATING SPINNERS
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.interests, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interest1.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.interests, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interest2.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.interests, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interest3.setAdapter(adapter3);
    }

    public void addExtraData(String email, String name, String contact, String in1, String in2, String in3) {
        FirebaseUser user = mAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        userID = user.getUid();

        User userProfile = new User(userID, name, email, 0.0, contact);
        ArrayList<String> interests = new ArrayList<>();
        interests.add(in1);
        interests.add(in2);
        interests.add(in3);
        userProfile.setInterests(interests);
        mDatabaseReference.child("users").child(userID).setValue(userProfile);
    }

    public void register(View view) {
        final String name = fullName.getText().toString();
        final String email = emailAddress.getText().toString();
        final String contact = contactNumber.getText().toString();
        String pwd = password.getText().toString();

        final String in1 = interest1.getSelectedItem().toString();
        final String in2 = interest2.getSelectedItem().toString();
        final String in3 = interest3.getSelectedItem().toString();

        if (Helper.validateEmail(email) && Helper.validateStrongPassword(pwd))
            mAuth.createUserWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                addExtraData(email, name, contact, in1, in2, in3);
                                Intent homeActivity = new Intent(RegisterActivity.this, HomeActivity.class);
                                startActivity(homeActivity);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registration not successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        else {
            Toast.makeText(this, "Email/Password is in incorrect format.", Toast.LENGTH_SHORT).show();
        }

    }
}
