package com.example.lab16;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.example.lab16.data.TaskData;

import java.util.ArrayList;
import java.util.List;

public class AddTask extends AppCompatActivity {

    private String[] mStates = new String[]{"New", "In-Progress", "Complete", "Assigned"};

    private String[] mTeams = new String[]{"TeamOne", "TeamTwo", "TeamThree"};

    public static final String TASK_ID = "taskId";
    public static final String TEAMNAME = "teamName";
    public static final String DATA = "data";

    private Handler handler;



    public static final String TAG = AddTask.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


        /// Spinner + Adapter for status


        Spinner stateSelector = findViewById(R.id.spinner_State_selector);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.state,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);


        stateSelector.setAdapter(spinnerAdapter);
        stateSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(
                        AddTask.this,
                        "The item selected is => " + mStates[i], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


            //  Spinner + Adapter for Team


        List<Team> teamsList = new ArrayList<>();
        Spinner teamSelector = findViewById(R.id.spinner_Team_selector);


        Amplify.API.query(
                ModelQuery.list(Team.class),
                detect -> {
                    for (Team team : detect.getData()) {
                        teamsList.add(team);
                    }
                    handler = new Handler(Looper.getMainLooper(), msg -> {
//                    runOnUiThread(() -> {
                        // create adapter

                        ArrayAdapter<CharSequence> spinnerAdapterTeam = new ArrayAdapter<CharSequence>(
                                this,
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                mTeams
                        );

                        // set adapter
                        teamSelector.setAdapter(spinnerAdapterTeam);
                        return true;
                    });
                    Bundle bundle = new Bundle();
                    bundle.putString("TeamTaskID", detect.toString());

                    Message message = new Message();
                    message.setData(bundle);

                    handler.sendMessage(message);
                },
                error -> {
                });




        Button button = findViewById(R.id.ADD_SUBMIT);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText addTitle = findViewById(R.id.ADD_TITLE);
                EditText AddBody = findViewById(R.id.ADD_BODY);
                Spinner stateSelector = findViewById(R.id.spinner_State_selector);
                Spinner teamSelector = findViewById(R.id.spinner_Team_selector);


                String title = addTitle.getText().toString();
                String body = AddBody.getText().toString();
                String state = stateSelector.getSelectedItem().toString();
                String teamName = teamSelector.getSelectedItem().toString();



                ///   Save the Task in DB


                Amplify.API.query(
                        ModelQuery.list(Team.class),
                        item -> {
                            for (Team team : item.getData()) {
                                if (teamName.equals(team.getName())) {
//                            saveTasks(team);
                                    Task task = Task.builder()
                                            .title(title)
                                            .description(body)
                                            .status(state)
                                            .teamTasksId(team.getId())
                                            .build();

                                    Amplify.API.mutate(
                                            ModelMutation.create(task),
                                            success -> {},
                                            error -> { });
                                }
                            }
                        },
                        error -> {}
                );


                Toast.makeText(getApplicationContext(), "Submitted!" + getTitle(), Toast.LENGTH_SHORT).show();


                Intent BackToMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(BackToMain);

            }
        });


    }

}


