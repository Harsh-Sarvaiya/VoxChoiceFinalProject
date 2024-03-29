package com.example.voxchoicefinalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voxchoicefinalproject.model.Poll;

import java.util.List;

public class PollAdapter extends RecyclerView.Adapter<PollViewHolder> {
    private List<Poll> polls;
    private OnItemClickListener listener;
    private boolean isTeacher;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public PollAdapter(List<Poll> polls, boolean isTeacher) {
        this.polls = polls;
        this.isTeacher = isTeacher;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public Poll getPoll(int position) {
        if (position >= 0 && position < polls.size()) {
            return polls.get(position);
        }
        return null;
    }

    @Override
    public PollViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poll_question_layout, parent, false);
        return new PollViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PollViewHolder holder, int position) {
        Poll poll = polls.get(position);
        String question = poll.getQuestion();
        holder.textQuestion.setText(question);

        if(!isTeacher) {
            holder.btnDelete.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition(); // Get the current position
                if (listener != null && clickedPosition != RecyclerView.NO_POSITION) {
                    listener.onItemClick(clickedPosition);
                }
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition(); // Get the current position
                if (listener != null && clickedPosition != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(clickedPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return polls.size();
    }
}
