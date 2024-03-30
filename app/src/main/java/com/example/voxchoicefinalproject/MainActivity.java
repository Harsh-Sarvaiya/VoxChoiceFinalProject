package com.example.voxchoicefinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_page_layout);



        Button btnNeither = findViewById(R.id.btnNeither);
        Button btnTeacher = findViewById(R.id.btnTeacher);
        Button btnStudent = findViewById(R.id.btnStudent);

        btnTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Teacher
                Intent intent = new Intent(MainActivity.this, TeacherRegister.class);
                startActivity(intent);
            }
        });

        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Student
                Intent intent = new Intent(MainActivity.this, StudentRegister.class);
                startActivity(intent);
            }
        });


        btnNeither.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Nothing
                Intent intent = new Intent(MainActivity.this, NeitherActivity.class);
                startActivity(intent);
            }
        });
    }
}