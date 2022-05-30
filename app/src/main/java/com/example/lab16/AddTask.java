package com.example.lab16;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.example.lab16.data.TaskData;

public class AddTask extends AppCompatActivity {

    private String[] mStates = new String[]{"New", "In-Progress", "Complete", "Assigned"};

    public static final String TAG = AddTask.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


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


        Button button = findViewById(R.id.ADD_SUBMIT);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText addTitle = findViewById(R.id.ADD_TITLE);
                EditText AddBody = findViewById(R.id.ADD_BODY);
                Spinner stateSelector = findViewById(R.id.spinner_State_selector);

//                TextView addState = findViewById(R.id.State);

                Intent passedIntent = getIntent();

                String title = addTitle.getText().toString();
                String body = AddBody.getText().toString();
                String state = stateSelector.getSelectedItem().toString();


                TaskData task = new TaskData(title, body, state);
                Long newTaskId = AppDatabase.getInstance(getApplicationContext()).taskDao().insertTask(task);

                System.out.println("******************** Task ID = " + newTaskId + " ************************");


                configureAmplify();


                Task item = Task.builder()
                        .title(title)
                        .description(body)
                        .status(state)
                        .build();



                Amplify.API.mutate(
                        ModelMutation.create(item),
                        success -> Log.i(TAG, "Saved item: " + success.getData().getTitle()),
                        error -> Log.e(TAG, "Could not save item to API", error)
                );



                // Datastore and API sync

                Amplify.DataStore.observe(Task.class,
                        started -> {
                            Log.i(TAG, "Observation began.");
                            // TODO: 5/17/22 Update the UI thread with in this call method
                            // Manipulate your views

                            // call handler
                        },
                        change -> Log.i(TAG, change.item().toString()),
                        failure -> Log.e(TAG, "Observation failed.", failure),
                        () -> Log.i(TAG, "Observation complete.")
                );

                Toast.makeText(getApplicationContext(), "Submitted!" +  getTitle(), Toast.LENGTH_SHORT).show();



                Intent BackToMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(BackToMain);

            }
        });



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

