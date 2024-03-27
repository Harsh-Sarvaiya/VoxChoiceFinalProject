package com.example.voxchoicefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.voxchoicefinalproject.model.Poll;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CreatePollActivity extends AppCompatActivity {
    private EditText editTextNumOptions;
    private LinearLayout optionsContainer;
    private EditText editTextQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_create_layout);

        editTextQuestion = findViewById(R.id.editTextQuestion); // Initialize editTextQuestion
        editTextNumOptions = findViewById(R.id.editTextNumOptions);
        optionsContainer = findViewById(R.id.optionsContainer);
        Button buttonCreatePoll = findViewById(R.id.btnCreatePollFinal);

        // Dynamic options layout
        editTextNumOptions.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    int numOptions = Integer.parseInt(editTextNumOptions.getText().toString());

                    optionsContainer.removeAllViews();

                    for (int i = 0; i < numOptions; i++) {
                        EditText optionEditText = new EditText(CreatePollActivity.this);
                        optionEditText.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        optionEditText.setHint("Enter option " + (i + 1));
                        optionsContainer.addView(optionEditText);
                    }
                }
            }
        });

        buttonCreatePoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the question from the EditText field
                String question = editTextQuestion.getText().toString();

                // Create a list to hold the options
                List<String> options = new ArrayList<>();
                for (int i = 0; i < optionsContainer.getChildCount(); i++) {
                    EditText optionEditText = (EditText) optionsContainer.getChildAt(i);
                    options.add(optionEditText.getText().toString());
                }

                // Get a reference to the Firebase database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference pollsRef = database.getReference("polls");

                // Generate a unique key for the new poll
                String pollId = pollsRef.push().getKey();

                Poll poll = new Poll(pollId, question, options);

                pollsRef.child(pollId).setValue(poll, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            // Poll saved successfully, store the poll ID
                            PollIdHolder.setPollId(pollId);

                            // Provide feedback to the user (optional)
                            Toast.makeText(CreatePollActivity.this, "Poll created successfully", Toast.LENGTH_SHORT).show();

                            // Optionally, you can finish the activity or reset the UI after creating the poll
                            // finish(); // Finish the activity
                            // resetUI(); // Reset the UI fields for creating a new poll
                            finish();

//                            // Start the next activity
//                            Intent intent = new Intent(CreatePollActivity.this, VotePollActivity.class);
//                            intent.putExtra("pollId", pollId);
//                            intent.putExtra("question", question);
//                            startActivity(intent);
                        } else {
                            // Handle database error
                            Toast.makeText(CreatePollActivity.this, "Failed to create poll: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
