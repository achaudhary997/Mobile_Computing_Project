package com.example.anubhav.mc_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    private Button loginButton;
    private EditText loginEmail, loginPassword;
    private TextView registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeElements();
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

    private void initializeElements() {
        loginButton = (Button)findViewById(R.id.loginButton);
        loginEmail = (EditText)findViewById(R.id.loginEmail);
        loginPassword = (EditText)findViewById(R.id.loginPassword);
        registerButton = (TextView)findViewById(R.id.registerButton);
    }

    private void login() {
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();
        if (validate(email, password)) {
            ;
        } else {
            ;
        }
    }

    private void register() {
        ;
    }

    private boolean validate(String email, String password) {
        return true;
    }
}
