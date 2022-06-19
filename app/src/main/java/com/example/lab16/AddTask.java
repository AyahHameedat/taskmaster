package com.example.lab16;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddTask extends AppCompatActivity {

    private static String URL;
    public static final int REQUEST_CODE = 123;
    private String[] mStates = new String[]{"New", "In-Progress", "Complete", "Assigned"};

    private String[] mTeams = new String[]{"TeamOne", "TeamTwo", "TeamThree"};

    public static final String TASK_ID = "taskId";
    public static final String TEAMNAME = "teamName";
    public static final String DATA = "data";
    private String imageName = null;
    private Handler handler;



    public static final String TAG = AddTask.class.getSimpleName();
    private Button uploadButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);



        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("OpenedMyApp")
                .addProperty("Successful", true)
                .addProperty("ProcessDuration", 792)
                .build();

        Amplify.Analytics.recordEvent(event);


        // Upload Image
        setUpListeners();




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
                                            .image(imageName)
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




    private void setUpListeners() {

        /// Upload Button
        uploadButton = findViewById(R.id.Upload_img);
//        uploadButton.setOnClickListener(view -> fileUpload());


        uploadButton.setOnClickListener(view -> pictureUpload());
//        uploadButton.setOnClickListener(view -> pictureDownload());
    }


    private void fileUpload()
    {
        File exampleFile = new File(getApplicationContext().getFilesDir(), "ExampleKey");

        // creates a file on the device
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(exampleFile));
            writer.append("Example file contents");
            writer.close();
        } catch (Exception exception) {
            Log.e(TAG, "Upload failed", exception);
        }

        // uploads the file
        Amplify.Storage.uploadFile(
                "ExampleKey",
                exampleFile,
                result -> Log.i(TAG, "Successfully uploaded: " + result.getKey()),
                storageFailure -> Log.e(TAG, "Upload failed", storageFailure)
        );
    }


    private void pictureUpload() {
        // Launches photo picker in single-select mode.
        // This means that the user can select one photo or video.
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        startActivityForResult(intent, REQUEST_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            // Handle error
            Log.e(TAG, "onActivityResult: Error getting image from device");
            return;
        }

//        File file = new File(data.getData().getPath());
//        fileName = file.getName();
//        fileData = data.getData();
//
        switch(requestCode) {
            case REQUEST_CODE:
                // Get photo picker response for single select.
                Uri currentUri = data.getData();
                File file = new File(data.getData().getPath());
                imageName = file.getName();
                // Do stuff with the photo/video URI.
                Log.i(TAG, "onActivityResult: the uri is => " + currentUri);

                try {
                    Bitmap bitmap = getBitmapFromUri(currentUri);
                    OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.close();

                    // upload to s3
                    // uploads the file
//                    image =
                    Amplify.Storage.uploadFile(
                            imageName,
                            file,
                            result -> {
//                                image = currentUri.getLastPathSegment();
//                                imageName = result.getKey();
                                Log.i(TAG, "Successfully uploaded: " + result.getKey());
                            },
                            storageFailure -> Log.e(TAG, "Upload failed", storageFailure)
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();

        return image;
    }



}


