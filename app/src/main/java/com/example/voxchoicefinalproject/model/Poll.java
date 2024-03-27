package com.example.voxchoicefinalproject.model;

// Poll.java

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Poll {
    private String id;
    private String question; // The question of the poll
    private List<String> options; // List of options for the poll
    private Map<String, Integer> votes; // Map to store the count of votes for each option

    public Poll() {
        // Default constructor required for Firebase
    }

    public Poll(String id, String question, List<String> options) {
        this.id = id;
        this.question = question;
        this.options = options;
        this.votes = new HashMap<>();
        // Initialize the vote count for each option to 0
        for (String option : options) {
            votes.put(option, 0);
        }
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public Map<String, Integer> getVotes() {
        return votes;
    }

    public void setVotes(Map<String, Integer> votes) {
        this.votes = votes;
    }

    // Method to increment the vote count for a given option
    public void voteForOption(String option) {
        if (votes.containsKey(option)) {
            int count = votes.get(option);
            votes.put(option, count + 1);
        }
    }
}
