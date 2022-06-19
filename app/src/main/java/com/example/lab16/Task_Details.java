package com.example.lab16;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.core.Amplify;
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



//    String image;

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


        String getTaskTitle = getIntent().getStringExtra("Title");
        String getTaskDescription = getIntent().getStringExtra("Description");
        String getTaskState = getIntent().getStringExtra("State");


        //** To Translate the Description **//
        translateDescription();

        //** To convert the text to speech **//
        Amplify.Predictions.convertTextToSpeech(
                getTaskDescription,
                result -> playAudio(result.getAudioData()),
                error -> Log.e("MyAmplifyApp", "Conversion failed", error)
        );


        String image = getIntent().getStringExtra("image");
        Log.i(TAG, "title: "+ getTaskTitle);
        Log.i(TAG, "onCreate: " + image);

        TextView title = findViewById(R.id.Title_taskDetails);
        TextView body = findViewById(R.id.Description_TaskDetails);
        TextView state = findViewById(R.id.State);


        title.setText(getTaskTitle);
        body.setText(getTaskDescription);
        state.setText(getTaskState);

        ImageView imageView = findViewById(R.id.imageFile);

        Intent i = getIntent();
//        image = intent.getStringExtra("image");
        Log.i(TAG, "ImageKey: " +  image);
//        getImage(image);

        Log.i(TAG, "print : " + getURL(image));
        String path = getURL(image);
        Log.i(TAG, "fun path: " + path);


        getImage(path);

    }




    private String getURL(String image)
    {
        AtomicReference<String> path = new AtomicReference<>("");
        new File(getApplicationContext().getFilesDir() + "/" + image + ".jpg");
        Amplify.Storage.getUrl(
                image,
                result -> {
                    path.set(result.getUrl().getFile());
                    Log.i(TAG, "The root path is: " + getApplicationContext().getFilesDir());
                    Log.i(TAG, "Successfully downloaded zoz: " + result.getUrl().getFile());
                    Log.i(TAG, "Successfully path zoz: " + path);

//                    if (imageName.endsWith("jpg") || imageName.endsWith("jpeg") ) {
//                    ImageView image = findViewById(R.id.imageFile);
                },
                error -> Log.e(TAG,  "Download Failure", error)
        );

        return path.get().toString();
    }
    private void getImage(String imageName) {

        Log.i(TAG, "getImage: " + imageName);
        new File(getApplicationContext().getFilesDir() + "/" + imageName + ".jpg");
        Amplify.Storage.getUrl(
                imageName,
                result -> {
                    Log.i(TAG, "The root path is: " + getApplicationContext().getFilesDir());
                    Log.i(TAG, "Successfully downloaded: " + result.getUrl().getFile());

//                    if (imageName.endsWith("jpg") || imageName.endsWith("jpeg") ) {
                        ImageView image = findViewById(R.id.imageFile);
                        Bitmap bitmap = BitmapFactory.decodeFile(result.getUrl().getPath());
                        image.setImageBitmap(bitmap);
                        Log.i("TaskMaster", "Successfully downloaded: " + result.getUrl().getFile());
                    },
                error -> Log.e(TAG,  "Download Failure", error)
        );
    }


    private void translateDescription() {

        Button btn_translate =  findViewById(R.id.btn_translate);
        btn_translate.setOnClickListener(view-> {

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




    private void pictureDownload() {
        Amplify.Storage.downloadFile(
                "image.jpg",
                new File(getApplicationContext().getFilesDir() + "/download.jpg"),
                result -> {
                    Log.i(TAG, "The root path is: " + getApplicationContext().getFilesDir());
                    Log.i(TAG, "Successfully downloaded: " + result.getFile().getName());
                },
                error -> Log.e(TAG,  "Download Failure", error)
        );
    }

}


//    String existingBucketName = "<your Bucket>";
//        String keyName = "/"+"";
//
//        AmazonS3 s3Client = null;
//        try {
//            s3Client = new AmazonS3Client(new PropertiesCredentials(
//                    Task_Details.class.getResourceAsStream("AwsCredentials.properties")));
//            GetObjectRequest request = new GetObjectRequest(existingBucketName,
//                    keyName);
//            S3Object object = s3Client.getObject(request);
//            S3ObjectInputStream objectContent = object.getObjectContent();
//            IOUtils.copy(objectContent, new FileOutputStream("D://upload//image.jpg"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }



//        Amplify.Storage.downloadFile(
//                image,
//                new File(getApplicationContext().getFilesDir() +image),
//                result -> {
//                    Log.i("download", "Successfully downloaded: " + result.getFile());
////                    ViewStructure link;
//                    if(result.getFile().toString().contains("image")) {
//                        Log.i("download", "0000000000000" + result.getFile());
//
//                        Bitmap bitmap = BitmapFactory.decodeFile(result.getFile().getPath());
//                        imageView.setImageBitmap(bitmap);
//                        imageView.setVisibility(View.VISIBLE);
//                    }
//                    },
//                error -> Log.e("download",  "Download Failure", error)
//        );


//
//        if (i.getExtras().getString("image") != null) {
//            Amplify.Storage.downloadFile(
//                    i.getExtras().getString("image"),
//                    new File(getApplicationContext().getFilesDir() + "/" + i.getExtras().getString("image") + ".jpg"),
//                    result -> {
//                        Bitmap bitmap = BitmapFactory.decodeFile(result.getFile().getPath());
//                        imageView.setImageBitmap(bitmap);
//                        Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getName());
//                    },
//                    error -> Log.e("MyAmplifyApp", "Download Failure", error)
//            );
//        }

//        Amplify.Storage.getUrl(
//                image,
//                result ->
//                {
//                    Log.i("MyAmplifyApp", "Successfully generated: " + result.getUrl());
//                    Bitmap bitmap = BitmapFactory.decodeFile(result.getUrl().getPath());
//                    imageView.setImageBitmap(bitmap);
//                    Log.i(TAG, "onCreate: " + bitmap.getWidth());
//                    Log.i(TAG, "Bitmap: " + result.getUrl().getPath());
//                    Log.i(TAG, "Bara'a: " + result.getUrl().getPath().toString());
//                },
//                error -> Log.e("MyAmplifyApp", "URL generation failure", error)
//        );





//        if (image != null)
//        {
//            getImage(image);
//        }
//        else
//            Log.i(TAG, "onCreate: imageKey is null->" + image);

//        imageView.setVisibility(View.INVISIBLE);
//        getImage(imageView.toString());
//        String file = getIntent().getStringExtra("image");
//        image =

//        if (intent.getExtras().getString("imageAya") != null) {
//            Amplify.Storage.downloadFile(
//                    intent.getExtras().getString("imageAya"),
//                    new File(getApplicationContext().getFilesDir() + "/" + intent.getExtras().getString("imageAya") + ".jpg"),
//                    result -> {
//                        Bitmap bitmap = BitmapFactory.decodeFile(result.getFile().getPath());
//                        imageView.setImageBitmap(bitmap);
//                        Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getName());
//                    },
//                    error -> Log.e("MyAmplifyApp", "Download Failure", error)
//            );
//        }