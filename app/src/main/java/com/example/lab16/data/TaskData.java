package com.example.lab16.data;
<<<<<<< HEAD

public class TaskData {

    private final String title;
    private final String body;
=======
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
>>>>>>> origin
    private final String state;


    public TaskData(String title, String body, String state) {
        this.title = title;
        this.body = body;
        this.state = state;
    }

<<<<<<< HEAD
=======
    public int getId() {
        return id;
    }

>>>>>>> origin
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
