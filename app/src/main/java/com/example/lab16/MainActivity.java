package com.example.lab16;

import static com.amplifyframework.core.Amplify.configure;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;


import com.amazonaws.auth.AWSCognitoIdentityProvider;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.util.Environment;
import com.example.lab16.data.TaskData;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String TASK_ID = "taskId";
    public static final String Team_ID = "teamTaskId";
    public static final String DATA = "data";

    private Handler handler;


//    List<TaskData> taskDataList = new ArrayList<>();
//    Long newTaskId = AppDatabase.getInstance(getApplicationContext()).taskDao().insertTask(task);


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
            startTaskDetailsIntent.putExtra("Title", "Task1");

            startActivity(startTaskDetailsIntent);

        }
    };


    private TextView mMyTasks;
    private TextView mSettings;
    private TextView mAllTasks;
    private TextView mUserName;
    private TextView mTeamName;
    private TextView mTask1;
    List<Task>tasksListAdap = new ArrayList();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: Called");


        configureAmplify();

        mUserName = findViewById(R.id.txt_username);
        mTeamName = findViewById(R.id.txt_teamName);


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




        System.out.println("**********************************" + Team_ID);



        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String teamId = sharedPreferences.getString("teamId", "");

        List<Task> tasksList = new ArrayList();





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
        setUserAndTeamName();
        getTaskByTeam();
        getTasks();


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

    private void setUserAndTeamName() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserName.setText(sharedPreferences.getString(Settings.USERNAME, "No Username Set"));

//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mTeamName.setText(sharedPreferences.getString(Settings.TEAMNAME, "No Team Set"));

    }



    private void setTeams()
    {
        Team teamOne = Team.builder().name("TeamOne").build();
        Team teamTwo = Team.builder().name("TeamTwo").build();
        Team teamThree = Team.builder().name("TeamThree").build();

        Amplify.API.mutate(
                ModelMutation.create(teamOne),
                item -> Log.i("TaskMaster", "Added : " + item.getData().getId()),
                error -> Log.e("TaskMaster", "Create failed", error)
        );


        Amplify.API.mutate(
                ModelMutation.create(teamTwo),
                item -> Log.i("TaskMaster", "Added : " + item.getData().getId()),
                error -> Log.e("TaskMaster", "Create failed", error)
        );
        Amplify.API.mutate(
                ModelMutation.create(teamThree),
                item -> Log.i("TaskMaster", "Added : " + item.getData().getId()),
                error -> Log.e("TaskMaster", "Create failed", error)
        );

    }


    private void getTaskByTeam()
    {
        handler = new Handler(Looper.getMainLooper(), msg -> {
            RecyclerView recyclerView = findViewById(R.id.recycler_view);

            TaskRecyclerViewAdapter taskRecyclerViewAdapter = new TaskRecyclerViewAdapter(
                    tasksListAdap, position -> {
                Toast.makeText(
                        MainActivity.this,
                        "The Task clicked => " + tasksListAdap.get(position).getTitle(), Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(getApplicationContext(), Task_Details.class);
                intent.putExtra("id", tasksListAdap.get(position).getId());
                intent.putExtra("Title", tasksListAdap.get(position).getTitle());
                intent.putExtra("Description", tasksListAdap.get(position).getDescription());
                intent.putExtra("State", tasksListAdap.get(position).getStatus().toString());
                startActivity(intent);

            });

            recyclerView.setAdapter(taskRecyclerViewAdapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            return true;
        });
    }


    private void getTasks()
    {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String teamName = sharedPreferences.getString(Settings.TEAMNAME, "Team");
        Log.i(TAG, "teamname: " + teamName);
        String[] teamId = new String[1];
        Amplify.API.query(ModelQuery.list(Team.class, Team.NAME.eq(teamName)),
                detect -> {
                    for (Team team :
                            detect.getData()) {
                        teamId[0] = team.getId();
                    }
                    Amplify.API.query(ModelQuery.list(Task.class),
                            item -> {
//                                        List <Task> helper = item.getData();
//                                        Log.i(TAG, "item: " + item.getData());
                                if (item.hasData()) {
//                                            Log.i(TAG, "item: " + item.getData());
                                    for (Task task : item.getData()) {
                                        Log.i(TAG, "item: " + task.getTeamTasksId());
                                        if (task.getTeamTasksId().equals(teamId[0])) {
                                            tasksListAdap.add(task);
                                            Log.i(TAG, "Eqnul " + task);

                                        }
                                    }
                                    Log.i(TAG, "tasskLisAdap " + tasksListAdap);
                                }
                                Log.i(TAG, "tasskLis " + tasksListAdap);

                                Bundle bundle = new Bundle();
                                bundle.putString("TeamTaskID", detect.toString());

                                Message message = new Message();
                                message.setData(bundle);

                                handler.sendMessage(message);
                                Log.i(TAG, "yooshi: " + tasksListAdap);
                            },
                            error -> Log.e(TAG, error.toString(), error)
                    );
                },
                error -> Log.e(TAG, error.toString(), error)
        );

    }


    private void deleteTask(List<TaskData> taskList)
    {
        for (int i = 0; i < taskList.size(); i++) {
            AppDatabase.getInstance(getApplicationContext()).taskDao().delete(taskList.get(i));
        }
    }



    private void configureAmplify () {
        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());
            Log.i(TAG, "Initialized Amplify");
        } catch (AmplifyException e) {
            Log.e(TAG, "Could not initialize Amplify", e);
        }
    }



}