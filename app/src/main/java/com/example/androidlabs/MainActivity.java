package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String ACTIVITY_NAME = "MAIN_ACTIVITY";
    private SharedPreferences mPreferences; // creating a reference to shared preferences object
    private SharedPreferences.Editor mEditor; // an editor for the SharedPreferences object
    private EditText mEmail;
    private static final String SHARED_PREF ="sharedPrefs";
    private static final String TEXT ="text";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Log.d(ACTIVITY_NAME, "In function: onCreate()");

        mEmail=(EditText) findViewById(R.id.editText);
        loadData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }


    public void loadData(){
        mPreferences=getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        String sharedPrefEmail = mPreferences.getString(TEXT,"");
        //mEmail.setText(mPreferences.getString(TEXT,""));
        mEmail.setText(sharedPrefEmail);
        Log.d("Email from sharedPref: ",sharedPrefEmail);
        //Log.d(ACTIVITY_NAME, "SharedPreferences saved email address is loaded successfully");
    }

    public void saveData(){
        mPreferences=getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        mEditor=mPreferences.edit();
        mEditor.putString(TEXT,mEmail.getText().toString());
        mEditor.commit();
        Log.d(ACTIVITY_NAME, "Email address is successfully saved using SharedPreferences");
    }

    public void buttonClickActivity(View args0){
        saveData();
        Intent i = new Intent(MainActivity.this,ProfileActivity.class);
        String emailToSend = mEmail.getText().toString();
        i.putExtra("emailSent", emailToSend);
        Log.d("Email passed: ",emailToSend);
        startActivity(i);
    }

}
