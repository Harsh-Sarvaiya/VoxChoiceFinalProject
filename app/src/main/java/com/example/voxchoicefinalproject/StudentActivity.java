package com.example.voxchoicefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voxchoicefinalproject.model.Poll;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_view_layout);

        Button btnStudentLogout = findViewById(R.id.btnStudentLogout);

        btnStudentLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), StudentLogin.class));
                finish();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewPolls);

        // Fetch data from the database
        fetchDataFromDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data here
        fetchDataFromDatabase();
    }

    private void fetchDataFromDatabase() {
        DatabaseReference pollsRef = FirebaseDatabase.getInstance().getReference("polls");

        pollsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<Poll> polls = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Poll poll = snapshot.getValue(Poll.class);
                    polls.add(poll);
                }

                // Create and set the adapter
                PollAdapter adapter = new PollAdapter(polls);

                // Set item click listener
                adapter.setOnItemClickListener(new PollAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        // Retrieve the clicked poll
                        Poll clickedPoll = polls.get(position);

                        // Start VotePollActivity and pass the poll data
                        Intent intent = new Intent(StudentActivity.this, VotePollActivity.class);
                        intent.putExtra("pollId", clickedPoll.getId());
                        intent.putExtra("question", clickedPoll.getQuestion());
                        startActivity(intent);
                    }
                });

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(StudentActivity.this));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(StudentActivity.this, "Failed to retrieve poll data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Handle back button click
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
