package com.example.lab16.data;

public class TaskData {


    public enum State {
        New,
        ASSIGNED,
        In_Progress,
        Complete
    }

    private final String title;
    private final String body;
    private final State state;


    public TaskData(String title, String body, State state) {
        this.title = title;
        this.body = body;
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public State getState() {
        return state;
    }
}
