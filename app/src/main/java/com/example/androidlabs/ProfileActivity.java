package com.example.androidlabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    EditText emailText;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageButton mImageButton;
    String emailReceived;
    Button btnToolbar, btnGoToChat;
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        Log.e(ACTIVITY_NAME, "In function: onCreate()");

        receiveEmailAddress();
        mImageButton = (ImageButton) findViewById(R.id.imageButton2);

        btnGoToChat = findViewById(R.id.BtnGoToChat);
        btnGoToChat.setOnClickListener( v -> {
            Intent goToChatIntent = new Intent(ProfileActivity.this,ChatRoomActivity.class);
            startActivity(goToChatIntent);
        } );

        btnToolbar = findViewById(R.id.btnToolbar);
        btnToolbar.setOnClickListener( v -> {
            Intent goToToolbarPage = new Intent(ProfileActivity.this,TestToolbar.class);
            startActivity(goToToolbarPage);
        } );

    }

    public void receiveEmailAddress(){
        emailReceived = getIntent().getStringExtra("emailSent");
        Log.d("Retrieved email: ",emailReceived);
        emailText = (EditText) findViewById(R.id.editText4);
        emailText.setText(emailReceived);
    }


    public void dispatchTakePictureIntent(View args0) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(ACTIVITY_NAME, "In function: onActivityResult()");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //mImageButton.setheig
            mImageButton.setImageBitmap(imageBitmap);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME, "In function:onStart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME, "In function:onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "In function:onDestroy()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME, "In function:onPause()");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e(ACTIVITY_NAME, "In function:onResume()");
    }

    /*
    public void goToChat(View args0) {
        Intent goToChatIntent = new Intent(ProfileActivity.this,ChatRoomActivity.class);
        startActivity(goToChatIntent);
    }
    */


    public void goToWeather(View args0) {
        Intent goToWeatherIntent = new Intent(ProfileActivity.this,WeatherForecast.class);
        startActivity(goToWeatherIntent);
    }



}
