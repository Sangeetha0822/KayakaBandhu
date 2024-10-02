package com.android.project.activitycontrollers.rozgarsewak;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.project.R;
import com.android.project.activitycontrollers.labourer.LabourerHomeActivity;
import com.android.project.database.AppDatabaseHelper;
import com.android.project.model.Labourer;
import com.android.project.model.RozgarSewak;
import com.android.project.utility.AppInstance;
import com.android.project.utility.Constants;

import java.util.ArrayList;

public class RozgarSewakLoginActivity extends AppCompatActivity {
    private EditText usernameET,passwordET;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rozgarsewaklogin);
        initializeUIComponents();
    }

    public void initializeUIComponents() {
        usernameET = findViewById(R.id.userName);
        passwordET = findViewById(R.id.password);
    }

    public void login(View view)
    {
        String username = usernameET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        if (username.length() == 0)
        {
            usernameET.setError(Constants.MISSING_PASSWORD);
            usernameET.requestFocus();
        }
        else if(password.length() == 0)
        {
            passwordET.setError(Constants.MISSING_PASSWORD);
            passwordET.requestFocus();
        }
        else{
            AppDatabaseHelper dataBaseHelper = new AppDatabaseHelper(this);

            RozgarSewak sewak = dataBaseHelper.getRozgarSewakWithUsernameAndPassword(username, password);

            if (sewak == null) {
                Toast.makeText(getApplicationContext(), Constants.INVALID_ADMIN_CREDENTIALS,Toast.LENGTH_SHORT).show();
            } else {
                ((AppInstance) getApplicationContext()).setCurrentRozgarSewak(sewak);
                Intent intent = new Intent(this, RozgarSewakHomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    public void forgotPassword(View view)
    {
        String userName = usernameET.getText().toString().trim();

        if (userName.length() == 0)
        {
            usernameET.setError(Constants.MISSING_USERNAME);
            usernameET.requestFocus();
        }
        else {
            AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
            RozgarSewak sewak = databaseHelper.getRozgarSewakWithUsername(userName);
            if (sewak == null) {
                Toast.makeText(getApplicationContext(), Constants.INVALID_USER, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), Constants.FORGOT_PASSWORD, Toast.LENGTH_LONG).show();
                final String passwordString = "Hello " + sewak.getName() + ". \nYour registered password is: " + sewak.getPassword() + "\nTeam Kayaka Bandhu App";

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
                    sendSms(sewak.getMobile(), passwordString);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.about_menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId())
        {
            case R.id.about:
                android.app.AlertDialog.Builder dailogBuilder = new android.app.AlertDialog.Builder(this);
                dailogBuilder.setIcon(R.drawable.applogo);
                dailogBuilder.setTitle(R.string.app_name);
                dailogBuilder.setMessage(Constants.APP_DESCRIPTION);
                dailogBuilder.create();
                dailogBuilder.show();
                return true;
        }

        return false;
    }


}


