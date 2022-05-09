package com.example.lab16;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Task_Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);


        String getTaskTitle = getIntent().getStringExtra("Title");
        String getTaskState = getIntent().getStringExtra("Task_State");

        TextView title = findViewById(R.id.Title_taskDetails);
        TextView state = findViewById(R.id.state);

        title.setText(getTaskTitle);
        state.setText(getTaskState);

    }
}