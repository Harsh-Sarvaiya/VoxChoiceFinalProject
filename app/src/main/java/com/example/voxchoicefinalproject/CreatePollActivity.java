package com.example.voxchoicefinalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class CreatePollActivity extends AppCompatActivity {
    private EditText editTextNumOptions;
    private LinearLayout optionsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_create_layout);

        editTextNumOptions = findViewById(R.id.edit_text_num_options);
        optionsContainer = findViewById(R.id.options_container);
        Button buttonCreatePoll = findViewById(R.id.button_create_poll);

        editTextNumOptions.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // Get the number of options entered by the user
                    int numOptions = Integer.parseInt(editTextNumOptions.getText().toString());
                    // Clear any existing EditText fields in the container
                    optionsContainer.removeAllViews();
                    // Add EditText fields dynamically based on the number of options
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
                // Handle the click on the "Create Poll" button
                // Add your logic to create the poll here
            }
        });
    }

}
