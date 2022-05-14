package com.example.lab16;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab16.data.TaskData;

public class AddTask extends AppCompatActivity {

    private String[] mStates = new String[]{"New", "In-Progress", "Complete", "Assigned"};

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

//        button.setOnClickListener(view -> {
//            Toast.makeText(this, "Submitted!", Toast.LENGTH_SHORT).show();
//
//        });

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

//                addTitle.setText(title);
//                AddBody.setText(body);
//                addState.setText(state);


                TaskData task = new TaskData(title, body, state);
                Long newTaskId = AppDatabase.getInstance(getApplicationContext()).taskDao().insertTask(task);

                System.out.println("******************** Task ID = " + newTaskId + " ************************");

//                Toast.makeText(this, "Submitted!" + task.getTitle(), Toast.LENGTH_SHORT).show();

                Intent BackToMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(BackToMain);

            }
        });



    }




}