package com.example.voxchoicefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the content view to the teacher login layout

        Button btnCreatePoll = findViewById(R.id.btnCreatePoll);

        btnCreatePoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the layout for Student
                Intent intent = new Intent(TeacherActivity.this, CreatePollActivity.class);
                startActivity(intent);
            }
        });
    }
}
