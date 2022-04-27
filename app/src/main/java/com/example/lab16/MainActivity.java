package com.example.lab16;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    private View.OnClickListener mAddClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            mMyTasks.setText("MY TASKS");
            mMyTasks.setAllCaps(true);


            Intent startAddTaskIntent = new Intent(getApplicationContext(), AddTask.class);

            startAddTaskIntent.putExtra("ADD_TITLE", "Title");
            startAddTaskIntent.putExtra("ADD_BODY", "Description");
            startAddTaskIntent.putExtra("ADD_SUBMIT", "SUBMIT");
//
            Toast.makeText(getApplicationContext(), "submitted!", Toast.LENGTH_SHORT).show();

            startActivity(startAddTaskIntent);
            
        }
    };


        private View.OnClickListener mAllClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            mAllTasks.setText("All TASKS");
            mAllTasks.setAllCaps(true);

            Intent startAllTasksIntent = new Intent(getApplicationContext(), AllTasks.class);
            startActivity(startAllTasksIntent);

        }
    };
    
        private TextView mMyTasks;
        private TextView mSubmit;
        private TextView mAllTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // this is a good place to do initial
        // set up like click listeners etc
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: Called");


        Button AddTaskButton = findViewById(R.id.ADD_TASK);
        mMyTasks = findViewById(R.id.MY_TASKs);

        AddTaskButton.setOnClickListener(mAddClickListener);


//
//        Button submitButton = findViewById(R.id.ADD_SUBMIT);
//        mSubmit = findViewById(R.id.ADD_SUBMIT);
////
//        submitButton.setOnClickListener(mSubmitListener);



        ////////////////*********ALL Task Button**********//////////////////

        Button AllTaskButton = findViewById(R.id.ALL_TASKS);
        mAllTasks = findViewById(R.id.ALL_TASKS);

        AllTaskButton.setOnClickListener(mAllClickListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart: called");
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: called - The App is VISIBLE");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause: called");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop: called");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: called");
        super.onDestroy();
    }

}