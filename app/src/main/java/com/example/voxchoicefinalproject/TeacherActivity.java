package com.example.voxchoicefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voxchoicefinalproject.model.Poll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class TeacherActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_view_layout); // Set the content view to the teacher login layout

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerViewPolls);

        Button btnCreatePoll = findViewById(R.id.btnCreatePoll);
        Button btnTeacherLogout = findViewById(R.id.btnTeacherLogout);

        // Fetch data from the database
        fetchDataFromDatabase();

        btnCreatePoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherActivity.this, CreatePollActivity.class);
                startActivity(intent);
            }
        });

        btnTeacherLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), TeacherLogin.class));
                finish();
            }
        });
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
                PollAdapter adapter = new PollAdapter(polls, true);

                // Set item click listener
                adapter.setOnItemClickListener(new PollAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        // Retrieve the clicked poll
                        Poll clickedPoll = polls.get(position);

                        // Start VotePollActivity and pass the poll data
                        Intent intent = new Intent(TeacherActivity.this, TeacherVotePollActivity.class);
                        intent.putExtra("pollId", clickedPoll.getId());
                        intent.putExtra("question", clickedPoll.getQuestion());
                        startActivity(intent);
                    }

                    @Override
                    public void onDeleteClick(int position) {
                        // Handle delete button click
                        deletePoll(position);
                    }
                });

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(TeacherActivity.this));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(TeacherActivity.this, "Failed to retrieve poll data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deletePoll(int position) {
        Poll pollToDelete = ((PollAdapter) recyclerView.getAdapter()).getPoll(position);
        if (pollToDelete != null) {
            DatabaseReference pollRef = FirebaseDatabase.getInstance().getReference("polls").child(pollToDelete.getId());
            pollRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(TeacherActivity.this, "Poll deleted successfully", Toast.LENGTH_SHORT).show();
                        fetchDataFromDatabase();
                    } else {
                        Toast.makeText(TeacherActivity.this, "Failed to delete poll", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Handle back button click
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
