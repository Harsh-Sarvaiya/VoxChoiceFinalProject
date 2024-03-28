package com.example.voxchoicefinalproject;

public class PollIdHolder {
    private static String pollId;

    public static void setPollId(String id) {
        pollId = id;
    }

    public static String getPollId() {
        return pollId;
    }
}
