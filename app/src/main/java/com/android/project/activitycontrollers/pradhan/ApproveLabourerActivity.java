package com.android.project.activitycontrollers.pradhan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.project.R;
import com.android.project.activitycontrollers.UserSelectionActivity;
import com.android.project.database.AppDatabaseHelper;
import com.android.project.model.Labourer;
import com.android.project.utility.AppInstance;
import com.android.project.utility.Constants;

import java.util.ArrayList;


public class ApproveLabourerActivity extends AppCompatActivity {
    private TextView nameTV, mobileTV, ageTV, genderTV, gramPanchayathTV, labourerIDTV;
    private long labourerID;
    private Labourer labourer = null;
    private ImageView profileIV;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approvelabourer);
        initializeUIComponents();
        labourerID = getIntent().getLongExtra(Constants.ID_KEY, 0);

        displayData();
    }

    public void initializeUIComponents() {
        nameTV = findViewById(R.id.name);
        mobileTV = findViewById(R.id.mobile);
        labourerIDTV = findViewById(R.id.id);
        ageTV = findViewById(R.id.age);
        genderTV = findViewById(R.id.gender);
        gramPanchayathTV = findViewById(R.id.panchayath);
        profileIV = findViewById(R.id.profile);
    }

    public void displayData() {
        AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
        labourer = databaseHelper.getLabourerWithID(labourerID);

        if (labourer != null) {

            nameTV.setText(labourer.getName());
            mobileTV.setText(labourer.getMobile());
            ageTV.setText(String.valueOf(labourer.getAge()));
            genderTV.setText(labourer.getGender());
            gramPanchayathTV.setText(labourer.getGramPanchayath());
            labourerIDTV.setText(String.valueOf(labourerID));

            String path = labourer.getProfilePath();

            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if (null != bitmap) {
                profileIV.setImageBitmap(bitmap);
            } else {
                int resID = this.getResources().getIdentifier("noimage", Constants.DRAWABLE_RESOURCE, this.getPackageName());
                profileIV.setImageResource(resID);
            }

        } else {
            Toast.makeText(getApplicationContext(), Constants.NO_LABOURER_WITH_NAME, Toast.LENGTH_LONG).show();
        }
    }

    public void approve(View view) {
        AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
        labourer.setApproved(true);
        databaseHelper.updateLabourerStatus(labourer);
        Toast.makeText(getApplicationContext(), Constants.LABOURER_APPROVED_SUCCESSFULLY, Toast.LENGTH_SHORT).show();
        finish();

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        Constants.MY_PERMISSIONS_REQUEST_SEND_SMS);

                // MY_PERMISSIONS_REQUEST_SEND_SMS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            sendSms(labourer.getMobile(), "Pradhan has approved your registration request using Kayaka Bandhu App");
            // Permission has already been granted
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changePassword:
                Intent intent = new Intent(this, AdminChangePasswordActivity.class);
                startActivity(intent);
                return true;

            case R.id.logout:
                ((AppInstance) getApplicationContext()).setAdminUser(false);
                Intent i = new Intent(this, UserSelectionActivity.class);
                startActivity(i);
                finish();
                return true;
            case R.id.about:
                android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this);
                dialogBuilder.setIcon(R.drawable.applogo);
                dialogBuilder.setTitle(R.string.app_name);
                dialogBuilder.setMessage(Constants.APP_DESCRIPTION);
                dialogBuilder.create();
                dialogBuilder.show();
                return true;

        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_SEND_SMS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    sendSms(labourer.getMobile(), "Pradhan has approved your registration request using Kayaka Bandhu App");
                    // Permission has already been granted


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;

        }
    }

    private void sendSms(String phonenumber, String message) {
        try {

            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> msgArray = smsManager.divideMessage(message);

            smsManager.sendMultipartTextMessage(phonenumber, null, msgArray, null, null);
            //Toast.makeText(getApplicationContext(), "Message Sent",Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
