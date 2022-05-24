package com.example.lab16;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lab16.data.TaskData;

public class Task_Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);


//
//        SQLiteDatabase db = this.openOrCreateDatabase("TaskData", Context.MODE_PRIVATE, null);
//        Long id = null;
//        Cursor taskData = db.rawQuery(AppDatabase.getInstance(getApplicationContext()).taskDao().getTaskByID(id), null);


        String getTaskTitle = getIntent().getStringExtra("Title");
        String getTaskState = getIntent().getStringExtra("State");

        TextView title = findViewById(R.id.Title_taskDetails);
        TextView state = findViewById(R.id.State);

        title.setText(getTaskTitle);
        state.setText(getTaskState);

    }
}