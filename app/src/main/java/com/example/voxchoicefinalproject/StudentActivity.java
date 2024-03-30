package com.example.voxchoicefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

        recyclerView = findViewById(R.id.recyclerViewPolls);

        fetchDataFromDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

                PollAdapter adapter = new PollAdapter(polls, false);

                adapter.setOnItemClickListener(new PollAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Poll clickedPoll = polls.get(position);

                        Intent intent = new Intent(StudentActivity.this, StudentVotePollActivity.class);
                        intent.putExtra("pollId", clickedPoll.getId());
                        intent.putExtra("question", clickedPoll.getQuestion());
                        startActivity(intent);
                    }

                    @Override
                    public void onDeleteClick(int position) {

                    }

                });

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(StudentActivity.this));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(StudentActivity.this, "Failed to retrieve poll data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
