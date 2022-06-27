package com.example.lab16;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.predictions.models.LanguageType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicReference;


public class Task_Details extends AppCompatActivity {

    private static final String TAG = Task_Details.class.getSimpleName();
    private final MediaPlayer mp = new MediaPlayer();
    private ImageView taskImage;
    private Handler handler;
    private String imageName;
    private String title;
    private String body;
    private String status;

    String imageKey;


//    String image;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);


        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("OpenedMyApp")
                .addProperty("Successful", true)
                .addProperty("ProcessDuration", 792)
                .build();

        Amplify.Analytics.recordEvent(event);


        taskImage = findViewById(R.id.imageFile);
        Intent taskIntent = getIntent();


        TextView taskTitle = findViewById(R.id.Title_taskDetails);
        TextView taskBody = findViewById(R.id.Description_TaskDetails);
        TextView taskState = findViewById(R.id.State);

        String getTaskDescription = taskBody.getText().toString();


        Handler handler = new Handler(Looper.getMainLooper(), msg -> {

            taskTitle.setText(title);
            taskBody.setText(body);
            taskState.setText(status);
            if (imageKey != null) {
                getImage(imageKey);
            }

            Log.i(TAG, "image: " + imageKey);

            return true;
        });


        Amplify.API.query(
                ModelQuery.get(Task.class, taskIntent.getStringExtra("id")),
                detect -> {

                    Log.i("MyAmplifyApp", (detect.getData()).getTitle());

                    title = detect.getData().getTitle();
                    body = detect.getData().getDescription();
                    status = detect.getData().getStatus();
                    imageKey = detect.getData().getImage();


                    Bundle bundle = new Bundle();


                    Message message = new Message();
                    message.setData(bundle);
                    handler.sendMessage(message);


                },
                error -> Log.e("MyAmplifyApp", error.toString(), error)
        );


        //** To Translate the Description **//
        translateDescription();

        //** To convert the text to speech **//

        Button playSound = findViewById(R.id.btn_playSound);
        playSound.setOnClickListener(view -> {
            Amplify.Predictions.convertTextToSpeech(
                    getTaskDescription,
                    result -> playAudio(result.getAudioData()),
                    error -> Log.e(TAG, "Conversion failed", error)
            );
        });

    }


    private void getImage(String imageKey) {

        Amplify.Storage.downloadFile(
                imageKey,
                new File(getApplicationContext().getFilesDir() + "/" + imageKey),
                result -> {
                    Log.i(TAG, "The root path is: " + getApplicationContext().getFilesDir());
                    Log.i(TAG, "Successfully downloaded: " + result.getFile().getName());

                    ImageView image = findViewById(R.id.imageFile);
                    Bitmap bitmap = BitmapFactory.decodeFile(getApplicationContext().getFilesDir() + "/" + result.getFile().getName());
                    image.setImageBitmap(bitmap);


                },
                error -> Log.e(TAG, "Download Failure", error)
        );
    }


    private void translateDescription() {

        Button btn_translate = findViewById(R.id.btn_translate);
        btn_translate.setOnClickListener(view -> {

                    TextView mTranslatedDescription = findViewById(R.id.textTranslated);
                    String getTaskDescription = getIntent().getStringExtra("Description");
                    Amplify.Predictions.translateText(
                            getTaskDescription,
                            LanguageType.ENGLISH,
                            LanguageType.ARABIC,
                            result -> {
                                Log.i("MyAmplifyApp", result.getTranslatedText());
                                mTranslatedDescription.setText(result.getTranslatedText());
                                mTranslatedDescription.setEnabled(true);

                            },
                            error -> Log.e("MyAmplifyApp", "Translation failed", error)
                    );
                }
        );
    }

    private void playAudio(InputStream data) {
        File mp3File = new File(getCacheDir(), "audio.mp3");

        try (OutputStream out = new FileOutputStream(mp3File)) {
            byte[] buffer = new byte[8 * 1_024];
            int bytesRead;
            while ((bytesRead = data.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            mp.reset();
            mp.setOnPreparedListener(MediaPlayer::start);
            mp.setDataSource(new FileInputStream(mp3File).getFD());
            mp.prepareAsync();
        } catch (IOException error) {
            Log.e("MyAmplifyApp", "Error writing audio file", error);
        }
    }
}