package com.example.lab16;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.text.Editable;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;

public class Settings extends AppCompatActivity {

    private static final String TAG = Settings.class.getSimpleName();
    public static final String USERNAME = "username";
    public static final String TEAMNAME = "teamName";

    private EditText mUsernameTxt;
    private Spinner stateSelectorTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("OpenedMyApp")
                .addProperty("Successful", true)
                .addProperty("ProcessDuration", 792)
                .build();

        Amplify.Analytics.recordEvent(event);



        mUsernameTxt = findViewById(R.id.edit_text_username_setting);
        Button btnSave = findViewById(R.id.btn_save);


        stateSelectorTeam = findViewById(R.id.spinner_Team_selector);
        List<Team> teamsList = new ArrayList<>();

        Amplify.API.query(
                ModelQuery.list(Team.class),
                item -> {
                    for (Team team : item.getData()) {
                        teamsList.add(team);
                    }


                    runOnUiThread(() -> {
                        String[] team_name = new String[teamsList.size()];

                        for (int i = 0; i < teamsList.size(); i++) {
                            team_name[i] = teamsList.get(i).getName();
                        }
                        // create adapter

                        ArrayAdapter<CharSequence> spinnerAdapterTeam = new ArrayAdapter<CharSequence>(
                                this,
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                team_name
                        );

                        spinnerAdapterTeam.notifyDataSetChanged();
                        // set adapter
                        stateSelectorTeam.setAdapter(spinnerAdapterTeam);


                    });

                },
                error -> {}
        );



        btnSave.setOnClickListener(view -> {

        if (mUsernameTxt.getText().toString().length() >= 3) {
            saveUsernameTeam();
        } else {
            Toast.makeText(this, "Add a longer Username", Toast.LENGTH_SHORT).show();
        }


        View view2 = this.getCurrentFocus();
        if (view2 != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }


            Intent BackToMain = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(BackToMain);

        });


    mUsernameTxt.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }



        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!btnSave.isEnabled()) {
                btnSave.setEnabled(true);
            }

            if (editable.toString().length() == 0){
                btnSave.setEnabled(false);
            }

        }
    });
}

    private void saveUsernameTeam() {
        // get the text from the edit text
        String username = mUsernameTxt.getText().toString();
        String teamName = stateSelectorTeam.getSelectedItem().toString();

        // create shared preference object and set up an editor
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();

        // save the text to shared preferences
        preferenceEditor.putString(USERNAME, username);
        preferenceEditor.apply();

        preferenceEditor.putString(TEAMNAME, teamName);
        preferenceEditor.apply();

        Toast.makeText(this, "Username Saved", Toast.LENGTH_SHORT).show();
    }

}




















