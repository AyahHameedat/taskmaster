package com.example.lab16;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lab16.data.TaskData;

import java.util.ArrayList;
import java.util.List;

public class TaskRecyclerViewActivity extends AppCompatActivity {

    List<TaskData> taskDataList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_recycler_view_adapter);


        initialiseTaskData();


        // get the recycler view object

        RecyclerView recyclerView = findViewById(R.id.recycler_view);



        // create an adapter -> TaskRecyclerViewAdapterActivity

        TaskRecyclerViewAdapter taskRecyclerViewAdapterActivity = new TaskRecyclerViewAdapter(
           taskDataList, position -> {
            Toast.makeText(
                    TaskRecyclerViewActivity.this,
                    "The Task clicked => " + taskDataList.get(position).getTitle(),Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(), Task_Details.class)); /// ayaa

           });


        // set adapter on recycler view
        recyclerView.setAdapter(taskRecyclerViewAdapterActivity);


        // set other important properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    private void initialiseTaskData()
    {
        taskDataList.add(new TaskData("Lab28", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries.", "new"));
        taskDataList.add(new TaskData("Code_Challenge28", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries.", "in progress"));
        taskDataList.add(new TaskData("Read29", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries.", "completed"));
        taskDataList.add(new TaskData("Learning_Journal29", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries.", "assigned"));
    }
}