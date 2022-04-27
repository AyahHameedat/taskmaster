package com.example.lab16;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class AddTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        TextView addTitle = findViewById(R.id.ADD_TITLE);
        TextView AddBody = findViewById(R.id.ADD_BODY);

        Intent passedIntent = getIntent();

        String title = passedIntent.getStringExtra("ADD_TITLE");
        String body = passedIntent.getStringExtra("ADD_BODY");

        addTitle.setText(title);
        AddBody.setText(body);
    }



}