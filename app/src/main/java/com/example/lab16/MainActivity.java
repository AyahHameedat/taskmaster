package com.example.lab16;

import androidx.annotation.NonNull;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;


import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
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


        authSession("onCreate");


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



        ////////////////*********           UserName Lab36               **********//////////////////


//        Amplify.Auth.currentUser

        handler = new Handler(getMainLooper(), msg -> {
           String username = msg.getData().getString("username");
           mUserName.setText(username);
            authSessionUserName(username);
            return true;
        });


}

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUserAndTeamName();
        getTaskByTeam();
        getTasks();


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.btn_Settings:
                navigateToSettings();
                return true;
            case R.id.logout:
                logout();
                break;
            case R.id.reset:
                // TODO: 5/25/22 Implement reset password
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
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
                item -> {},
                error -> { }
        );


        Amplify.API.mutate(
                ModelMutation.create(teamTwo),
                item -> { },
                error -> { }
        );
        Amplify.API.mutate(
                ModelMutation.create(teamThree),
                item -> { },
                error -> { }
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


    private void getTasks() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String teamName = sharedPreferences.getString(Settings.TEAMNAME, "Team");

        String[] teamId = new String[1];
        Amplify.API.query(ModelQuery.list(Team.class, Team.NAME.eq(teamName)),
                detect -> {
                    for (Team team : detect.getData()) {
                        teamId[0] = team.getId();
                    }
                    Amplify.API.query(ModelQuery.list(Task.class),
                            item -> {
//                                        List <Task> helper = item.getData();
                                if (item.hasData()) {
                                    for (Task task : item.getData()) {
                                        if (task.getTeamTasksId().equals(teamId[0])) {
                                            tasksListAdap.add(task);
                                        }
                                    }
                                }

                                Bundle bundle = new Bundle();
                                bundle.putString("TeamTaskID", detect.toString());

                                Message message = new Message();
                                message.setData(bundle);

                                handler.sendMessage(message);
                            },
                            error -> {
                            }
                    );
                },
                error -> {}
        );

    }


    private void deleteTask(List<TaskData> taskList)
    {
        for (int i = 0; i < taskList.size(); i++) {
            AppDatabase.getInstance(getApplicationContext()).taskDao().delete(taskList.get(i));
        }
    }


    private void authSessionUserName(String method) {
//        https://github.com/aws-amplify/amplify-android/issues/851
        Amplify.Auth.fetchAuthSession(
                        result -> {
                        Log.i(TAG, result.toString());
                        Toast.makeText(this, "${Amplify.Auth.currentUser.userId} is logged in", Toast.LENGTH_LONG).show();
                },
                error -> Log.e(TAG, error.toString())
        );
    }


    private void authSession(String method) {
        Amplify.Auth.fetchAuthSession(
                result -> {
                    Log.i(TAG, "Auth Session => " + method + result.toString());
                    if(result.isSignedIn())
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("username", "username");

                        Message message = new Message();
                        message.setData(bundle);

                        handler.sendMessage(message);
                    }
                },

                error -> Log.e(TAG, error.toString())
        );
    }


    private void logout() {
        Amplify.Auth.signOut(
                () -> {
                    Log.i(TAG, "Signed out successfully");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    authSession("logout");
                    finish();
                },
                error -> Log.e(TAG, error.toString())
        );
    }

}