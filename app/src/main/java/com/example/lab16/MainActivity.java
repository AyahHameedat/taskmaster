package com.example.lab16;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab16.data.TaskData;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{


    private static final String TAG = MainActivity.class.getSimpleName();

    List<TaskData> taskDataList = new ArrayList<>();



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
            startTaskDetailsIntent.putExtra("Title","Task1");

            startActivity(startTaskDetailsIntent);

        }
    };


        private TextView mMyTasks;
        private TextView mSettings;
        private TextView mAllTasks;
        private TextView mUserName;
        private TextView mTask1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: Called");


        mUserName = findViewById(R.id.txt_username);




        ////////////////*********             ADD Task Button               **********//////////////////



        Button AddTaskButton = findViewById(R.id.ADD_TASK);
        mMyTasks = findViewById(R.id.MY_TASKs);

        AddTaskButton.setOnClickListener(mAddClickListener);



        ////////////////*********             ALL Task Button               **********//////////////////



//        Button AllTaskButton = findViewById(R.id.ALL_TASKS);
//        mAllTasks = findViewById(R.id.ALL_TASKS);
//
//        AllTaskButton.setOnClickListener(mAllClickListener);



        ////////////////*********             Settings Button                **********//////////////////



        Button btnSettings = findViewById(R.id.btn_Settings);
        mSettings = findViewById(R.id.btn_Settings);

        btnSettings.setOnClickListener(mClickSettings);





        ///////////****************** Recycler View      *****      Lab28     ******////////////////////

        initialiseTaskData();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);



        TaskRecyclerViewAdapter taskRecyclerViewAdapter = new TaskRecyclerViewAdapter(
                taskDataList, position -> {
            Toast.makeText(
                    MainActivity.this,
                    "The Task clicked => " + taskDataList.get(position).getTitle(),Toast.LENGTH_SHORT).show();


            Intent intent=new Intent(getApplicationContext(),Task_Details.class);
            intent.putExtra("Title",taskDataList.get(position).getTitle());
            intent.putExtra("Description",taskDataList.get(position).getBody());
            intent.putExtra("State",taskDataList.get(position).getState().toString());
            startActivity(intent);


        });

        recyclerView.setAdapter(taskRecyclerViewAdapter);


        // set other important properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


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



    private void initialiseTaskData()
    {
        taskDataList.add(new TaskData("Lab28", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries.", TaskData.State.New));
        taskDataList.add(new TaskData("Code_Challenge28", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries.", TaskData.State.ASSIGNED));
        taskDataList.add(new TaskData("Read29", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries.", TaskData.State.In_Progress));
        taskDataList.add(new TaskData("Learning_Journal29", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries.", TaskData.State.Complete));
    }


}