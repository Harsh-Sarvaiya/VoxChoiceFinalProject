package com.example.voxchoicefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextQuestion = findViewById(R.id.editTextQuestion);
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
                String question = editTextQuestion.getText().toString();

                if (question.isEmpty()) {
                    Toast.makeText(CreatePollActivity.this, "Please enter a question", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }

                List<String> options = new ArrayList<>();
                for (int i = 0; i < optionsContainer.getChildCount(); i++) {
                    EditText optionEditText = (EditText) optionsContainer.getChildAt(i);
                    options.add(optionEditText.getText().toString());
                }

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference pollsRef = database.getReference("polls");

                String pollId = pollsRef.push().getKey();

                Poll poll = new Poll(pollId, question, options);

                pollsRef.child(pollId).setValue(poll, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            PollIdHolder.setPollId(pollId);

                            Toast.makeText(CreatePollActivity.this, "Poll created successfully", Toast.LENGTH_SHORT).show();

                            finish();

                        } else {
                            Toast.makeText(CreatePollActivity.this, "Failed to create poll: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
