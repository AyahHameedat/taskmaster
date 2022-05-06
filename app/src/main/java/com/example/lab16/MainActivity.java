package com.example.lab16;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();


    private View.OnClickListener mAddClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            mMyTasks.setText("MY TASKS");
            mMyTasks.setAllCaps(true);


            Intent startAddTaskIntent = new Intent(getApplicationContext(), AddTask.class);

            startAddTaskIntent.putExtra("ADD_TITLE", "Title");
            startAddTaskIntent.putExtra("ADD_BODY", "Description");

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



    private View.OnClickListener mClickSettings = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            mSettings.setText("settings");
            mSettings.setAllCaps(true);

            Intent startAllTasksIntent = new Intent(getApplicationContext(), Settings.class);
            startActivity(startAllTasksIntent);

        }
    };


    private View.OnClickListener mClickTask1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            mTask1.setText("Task1");
            mTask1.setAllCaps(true);

            Intent startTaskDetailsIntent = new Intent(getApplicationContext(), Task_Details.class);
            startTaskDetailsIntent.putExtra("Title_taskDetails","Task1");

            startActivity(startTaskDetailsIntent);

        }
    };

    private View.OnClickListener mClickTask2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            mTask2.setText("Task2");
            mTask2.setAllCaps(true);

            Intent startTaskDetailsIntent = new Intent(getApplicationContext(), Task_Details.class);
            startTaskDetailsIntent.putExtra("Title_taskDetails","Task2");
            startActivity(startTaskDetailsIntent);

        }
    };

    private View.OnClickListener mClickTask3 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            mTask3.setText("Task3");
            mTask3.setAllCaps(true);

            Intent startTaskDetailsIntent = new Intent(getApplicationContext(), Task_Details.class);
            startTaskDetailsIntent.putExtra("Title_taskDetails","Task3");
            startActivity(startTaskDetailsIntent);

        }
    };

        private TextView mMyTasks;
        private TextView mSettings;
        private TextView mAllTasks;
        private TextView mUserName;
        private TextView mTask1;
        private TextView mTask2;
        private TextView mTask3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: Called");


        mUserName = findViewById(R.id.txt_username);


        ////////////////*********             ADD Task Button               **********//////////////////



//        Button AddTaskButton = findViewById(R.id.ADD_TASK);
//        mMyTasks = findViewById(R.id.MY_TASKs);
//
//        AddTaskButton.setOnClickListener(mAddClickListener);



        ////////////////*********             ALL Task Button               **********//////////////////



//        Button AllTaskButton = findViewById(R.id.ALL_TASKS);
//        mAllTasks = findViewById(R.id.ALL_TASKS);
//
//        AllTaskButton.setOnClickListener(mAllClickListener);



        ////////////////*********             Settings Button                **********//////////////////



        Button btnSettings = findViewById(R.id.btn_Settings);
        mSettings = findViewById(R.id.btn_Settings);

        btnSettings.setOnClickListener(mClickSettings);


        ////////////////*********             Task1 Button                **********//////////////////


        Button btnTask1 = findViewById(R.id.btn_task1);
        mTask1 = findViewById(R.id.btn_task1);

        btnTask1.setOnClickListener(mClickTask1);





        ////////////////*********             Task2 Button                **********//////////////////


        Button btnTask2 = findViewById(R.id.btn_task2);
        mTask2 = findViewById(R.id.btn_task2);

        btnTask2.setOnClickListener(view -> {
            Intent intent = new Intent(this,Task_Details.class);
            intent.putExtra("Title_taskDetails",btnTask2.getText());
            startActivity(intent);
        });

//        btnTask2.setOnClickListener(mClickTask2);




        ////////////////*********             Task3 Button                **********//////////////////


        Button btnTask3 = findViewById(R.id.btn_task3);
        mTask3 = findViewById(R.id.btn_task3);

        btnTask3.setOnClickListener(mClickTask3);


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
        super.onResume();
        Log.i(TAG, "onResume: called - The App is VISIBLE");
        setUsername();

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_Settings:
                navigateToSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void navigateToSettings() {
        Intent settingsIntent = new Intent(this, Settings.class);
        startActivity(settingsIntent);
    }

    private void setUsername() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mUserName.setText(sharedPreferences.getString(Settings.USERNAME, "No Username Set"));
    }

}