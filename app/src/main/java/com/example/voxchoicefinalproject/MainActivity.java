package com.example.voxchoicefinalproject;

import static com.example.voxchoicefinalproject.R.*;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String username, password;

    EditText usernameInput;
    EditText passwordInput;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_login);

        usernameInput = (EditText) findViewById(R.id.usernameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);

        loginButton = (Button) findViewById(id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener()[
            @Override
            public void onClick(View v) {

            }

        ]);
    }
}