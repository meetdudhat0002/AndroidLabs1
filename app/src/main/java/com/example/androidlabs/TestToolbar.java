package com.example.androidlabs;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

public class TestToolbar extends AppCompatActivity implements CustomDialog.CustomDialogListener {

    private static final String TAG = "TestToolbar";
    final Context context = this;
    private ActionMenuView amvMenu; //toolbar component
    private String userTypedMsg;
    //EditText editTextMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_test_toolbar);

        //txtMsg = findViewById(R.id.txtMsg);

        /**
         * Block for toolbar menu
         **/
        Toolbar testToolbar = findViewById(R.id.testToolbar);
        amvMenu = testToolbar.findViewById(R.id.amvMenu);

        amvMenu.setOnMenuItemClickListener( item -> onOptionsItemSelected(item));
        setSupportActionBar(testToolbar);
        //getSupportActionBar().setTitle(null);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /**
         *
         * Block for toolbar menu
         **/


}


    /**
     * Block for toolbar menu
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        // use amvMenu here
        inflater.inflate(R.menu.menu_items, amvMenu.getMenu());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.choice1:

                if(userTypedMsg!=null) {
                    if (!userTypedMsg.isEmpty()) {
                        Toast.makeText( getApplicationContext(), userTypedMsg, Toast.LENGTH_SHORT ).show();
                    } else {
                        Toast.makeText( getApplicationContext(), "This is the initial message", Toast.LENGTH_SHORT ).show();
                    }
                }
                else
                {
                    Toast.makeText( getApplicationContext(), "This is the initial message", Toast.LENGTH_SHORT ).show();
                }
                return true;

            case R.id.choice2:
                openDialog();
                return true;

            case R.id.choice3:
                /*
                 Snackbar code starts
                */
                Snackbar sbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Want to go back?", Snackbar.LENGTH_LONG);
                sbar.setAction("Go Back", new MyGoBackListener());
                sbar.show();
                /*
                 Snackbar code ends
                */
                /*
                 Snackbar NOTES:
                 setting view as getWindow().getDecorView() may cause snackbar to appear below the bottom bar
                 so, it's better to use getWindow().getDecorView().findViewById(android.R.id.content) as view
                */

                return true;

            case R.id.choice4:

                // code for the selected menu item's function
                Toast.makeText(getApplicationContext(),"You clicked on the overflow menu",Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    /**
     * Block for toolbar menu
     **/


    // Snackbar's action's onclick listener
    public class MyGoBackListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }



    public void openDialog() {
        CustomDialog customDialog = new CustomDialog();
        customDialog.show(getSupportFragmentManager(), "custom dialog");
    }

    @Override
    public void applyTexts(String msg) {
        userTypedMsg = msg; //now variable userTypedMsg has the typed message
        //textViewUsername.setText(username);
        //textViewPassword.setText(password);
    }


}
