package com.example.lab16.data;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class TaskData {


    public enum State {
        New,
        ASSIGNED,
        In_Progress,
        Complete
    }

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    private final String title;

    @ColumnInfo(name = "body")
    private final String body;

    @ColumnInfo(name = "state")
    private final String state;


    public TaskData(String title, String body, String state) {
        this.title = title;
        this.body = body;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getState() {
        return state;
    }
}
