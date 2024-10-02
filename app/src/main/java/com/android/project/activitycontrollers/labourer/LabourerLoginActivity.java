package com.android.project.activitycontrollers.labourer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.project.R;
import com.android.project.database.AppDatabaseHelper;
import com.android.project.model.Labourer;
import com.android.project.utility.AppInstance;
import com.android.project.utility.Constants;

import java.util.ArrayList;

public class LabourerLoginActivity extends AppCompatActivity {

    private EditText usernameET = null;
    private EditText passwordET = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labourerloginscreen);
    }
    public void signUp(View view)
    {
        Intent intent  = new Intent(this,LabourerSignupActivity.class);
        startActivity(intent);
    }
    public void login(View view) {
        usernameET = findViewById(R.id.userName);
        passwordET = findViewById(R.id.password);
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();

        AppDatabaseHelper dataBaseHelper = new AppDatabaseHelper(this);
        Labourer labourer = dataBaseHelper.getLabourerWithUsernameAndPassword(username, password);

        if (username.length() == 0)
        {
            usernameET.setError(Constants.MISSING_USERNAME);
            usernameET.requestFocus();
        }
        else if(password.length() == 0)
        {
            passwordET.setError(Constants.MISSING_PASSWORD);
            passwordET.requestFocus();
        }
        else {
            if (labourer == null) {
                Toast.makeText(this, "Register", Toast.LENGTH_LONG).show();
           }
            else{
                ((AppInstance) getApplicationContext()).setCurrentLabourer(labourer);
                Intent intent = new Intent(this, LabourerHomeActivity.class);
            intent.putExtra("id", labourer.getLabourerID());

            startActivity(intent);
            finish();
        }

        }
    }

    public void forgotPassword(View view)
    {
        usernameET = findViewById(R.id.userName);
        String userName = usernameET.getText().toString().trim();

        if (userName.length() == 0)
        {
            usernameET.setError(Constants.MISSING_USERNAME);
            usernameET.requestFocus();
        }
        else {
            AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
            Labourer labourer = databaseHelper.getLabourerWithUsername(userName);
            if (labourer == null) {
                Toast.makeText(getApplicationContext(), Constants.INVALID_USER, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), Constants.FORGOT_PASSWORD, Toast.LENGTH_LONG).show();
                final String passwordString = "Hello " + labourer.getName() + ". \nYour registered password is: " + labourer.getPassword() + "\nTeam Kayaka Bandhu App";

                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED)
                {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.SEND_SMS))
                    {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    }
                    else
                    {

                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.SEND_SMS},
                                Constants.MY_PERMISSIONS_REQUEST_SEND_SMS);

                        // MY_PERMISSIONS_REQUEST_SEND_SMS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }
                else
                {
                    sendSms(labourer.getMobile(), passwordString);
                    // Permission has already been granted
                }


            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_SEND_SMS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String userName = usernameET.getText().toString().trim();
                    AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
                    Labourer labourer = databaseHelper.getLabourerWithUsername(userName);


                    final String passwordString = "Hello " + labourer.getName() + ". \nYour registered password is: " + labourer.getPassword() + "\nTeam Kayaka Bandhu App";

                    sendSms(labourer.getMobile(), passwordString);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
        }
    }

    private void sendSms(String phonenumber, String message)
    {
        SmsManager manager = SmsManager.getDefault();

        int length = message.length();

        if(length > 160)
        {
            ArrayList<String> messagelist = manager.divideMessage(message);

            manager.sendMultipartTextMessage(phonenumber, null, messagelist, null, null);
        }
        else
        {
            manager.sendTextMessage(phonenumber, null, message, null, null);
        }
    }

}
