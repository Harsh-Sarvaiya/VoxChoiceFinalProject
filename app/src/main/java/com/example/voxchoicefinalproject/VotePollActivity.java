package com.example.voxchoicefinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VotePollActivity extends AppCompatActivity {

    private DatabaseReference pollRef;
//    String pollId = PollIdHolder.getPollId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_vote_layout);

        String pollId = getIntent().getStringExtra("pollId");

//        pollId = PollIdHolder.getPollId();

        if (pollId != null) {
            pollRef = FirebaseDatabase.getInstance().getReference("polls").child(pollId);
            retrievePollData();
        } else {
            // Handle null pollId gracefully
            Toast.makeText(this, "Poll ID is null", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if pollId is null
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
                    // Retrieve poll question and options from dataSnapshot
                    String question = dataSnapshot.child("question").getValue(String.class);
                    List<String> options = new ArrayList<>();
                    for (DataSnapshot optionSnapshot : dataSnapshot.child("options").getChildren()) {
                        String option = optionSnapshot.getValue(String.class);
                        options.add(option);
                    }

                    // Populate layout with poll question and options
                    TextView textViewQuestion = findViewById(R.id.textQuestion);
                    textViewQuestion.setText(question);

                    RadioGroup radioGroupOptions = findViewById(R.id.radioGroupOptions);
                    for (String option : options) {
                        RadioButton radioButton = new RadioButton(VotePollActivity.this);
                        radioButton.setText(option);
                        radioGroupOptions.addView(radioButton);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    private void submitVote() {
        RadioGroup radioGroupOptions = findViewById(R.id.radioGroupOptions);
        int selectedOptionId = radioGroupOptions.getCheckedRadioButtonId();
        if (selectedOptionId != -1) {
            // Get the selected option
            RadioButton selectedRadioButton = findViewById(selectedOptionId);
            final String selectedOption = selectedRadioButton.getText().toString();

            // Update vote count in Firebase Realtime Database
            pollRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Retrieve the Poll object from the dataSnapshot
                    Poll poll = dataSnapshot.getValue(Poll.class);
                    if (poll != null) {
                        // Update the vote count for the selected option in the Poll object
                        Map<String, Integer> votes = poll.getVotes();
                        if (votes.containsKey(selectedOption)) {
                            int currentVotes = votes.get(selectedOption);
                            votes.put(selectedOption, currentVotes + 1);

                            // Update the Poll object in Firebase
                            dataSnapshot.getRef().setValue(poll);

                            // Inform the user that the vote has been submitted
                            Toast.makeText(VotePollActivity.this, "Vote submitted!", Toast.LENGTH_SHORT).show();
                        } else {
                            // This shouldn't happen if the options are correctly synchronized with Firebase
                            Log.e("VotePollActivity", "Selected option not found in Poll object");
                        }
                    } else {
                        // Poll object is null
                        Log.e("VotePollActivity", "Poll object is null");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                    Toast.makeText(VotePollActivity.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Firebase Database Error", "Database Error: " + databaseError.getMessage(), databaseError.toException());
                }
            });

            finish();
        } else {
            // No option selected
            Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
        }
    }
}