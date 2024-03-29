package com.example.voxchoicefinalproject;

import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

public class PollViewHolder extends RecyclerView.ViewHolder {
    public TextView textQuestion;
    public Button btnDelete;

    public PollViewHolder(View itemView) {
        super(itemView);
        textQuestion = itemView.findViewById(R.id.textQuestion);
        btnDelete = itemView.findViewById(R.id.btnDelete);
    }
}
