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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    private static final String TAG = Settings.class.getSimpleName();
    public static final String USERNAME = "username";
    private EditText mUsernameTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mUsernameTxt = findViewById(R.id.edit_text_username_setting);
        Button btnSave = findViewById(R.id.btn_save);


        btnSave.setOnClickListener(view -> {
            Log.i(TAG, "Save button Clicked");

        if (mUsernameTxt.getText().toString().length() >= 3) {
            saveUsername();
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
            Log.i(TAG, "onTextChanged: the text is : " + charSequence);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            Log.i(TAG, "afterTextChanged: the final text is : " + editable.toString());
            if (!btnSave.isEnabled()) {
                btnSave.setEnabled(true);
            }

            if (editable.toString().length() == 0){
                btnSave.setEnabled(false);
            }

        }
    });
}

    private void saveUsername() {
        // get the text from the edit text
        String username = mUsernameTxt.getText().toString();

        // create shared preference object and set up an editor
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();

        // save the text to shared preferences
        preferenceEditor.putString(USERNAME, username);
        preferenceEditor.apply();

        Toast.makeText(this, "Username Saved", Toast.LENGTH_SHORT).show();
    }
}




















