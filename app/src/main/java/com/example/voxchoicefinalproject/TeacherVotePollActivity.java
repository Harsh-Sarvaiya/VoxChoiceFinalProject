package com.example.voxchoicefinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.voxchoicefinalproject.model.Poll;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TeacherVotePollActivity extends AppCompatActivity {

    private DatabaseReference pollRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_vote_poll_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String pollId = getIntent().getStringExtra("pollId");

        if (pollId != null) {
            pollRef = FirebaseDatabase.getInstance().getReference("polls").child(pollId);
            retrievePollData();
        } else {
            Toast.makeText(this, "Poll ID is null", Toast.LENGTH_SHORT).show();
            finish();
        }

        Button buttonVote = findViewById(R.id.buttonVote);
        buttonVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitVote();
            }
        });
    }

    private void retrievePollData() {
        pollRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String question = dataSnapshot.child("question").getValue(String.class);
                    List<String> options = new ArrayList<>();
                    for (DataSnapshot optionSnapshot : dataSnapshot.child("options").getChildren()) {
                        String option = optionSnapshot.getValue(String.class);
                        options.add(option);
                    }

                    TextView textViewQuestion = findViewById(R.id.textQuestion);
                    textViewQuestion.setText(question);

                    RadioGroup radioGroupOptions = findViewById(R.id.radioGroupOptions);
                    for (String option : options) {
                        RadioButton radioButton = new RadioButton(TeacherVotePollActivity.this);
                        radioButton.setText(option);
                        radioGroupOptions.addView(radioButton);

                        TextView voteCountTextView = new TextView(TeacherVotePollActivity.this);
                        int currentVotes = dataSnapshot.child("votes").child(option).getValue(Integer.class);
                        voteCountTextView.setText("Votes: " + currentVotes);
                        radioGroupOptions.addView(voteCountTextView);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(TeacherVotePollActivity.this, "Failed to cast vote: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitVote() {
        RadioGroup radioGroupOptions = findViewById(R.id.radioGroupOptions);
        int selectedOptionId = radioGroupOptions.getCheckedRadioButtonId();
        if (selectedOptionId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedOptionId);
            final String selectedOption = selectedRadioButton.getText().toString();

            pollRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Poll poll = dataSnapshot.getValue(Poll.class);
                    if (poll != null) {
                        Map<String, Integer> votes = poll.getVotes();
                        if (votes.containsKey(selectedOption)) {
                            int currentVotes = votes.get(selectedOption);
                            votes.put(selectedOption, currentVotes + 1);

                            // Update vote count TextView
                            for (int i = 0; i < radioGroupOptions.getChildCount(); i++) {
                                View view = radioGroupOptions.getChildAt(i);
                                if (view instanceof RadioButton && ((RadioButton) view).getText().toString().equals(selectedOption)) {
                                    TextView voteCountTextView = (TextView) radioGroupOptions.getChildAt(i + 1);
                                    voteCountTextView.setText("Votes: " + (currentVotes + 1));
                                    break;
                                }
                            }

                            dataSnapshot.getRef().setValue(poll);

                            Toast.makeText(TeacherVotePollActivity.this, "Vote submitted!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("VotePollActivity", "Selected option not found in Poll object");
                        }
                    } else {
                        Log.e("VotePollActivity", "Poll object is null");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(TeacherVotePollActivity.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Firebase Database Error", "Database Error: " + databaseError.getMessage(), databaseError.toException());
                }
            });

            finish();
        } else {
            Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}