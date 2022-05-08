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
        TextView title = findViewById(R.id.Title_taskDetails);
        title.setText(getTaskTitle);

    }
}