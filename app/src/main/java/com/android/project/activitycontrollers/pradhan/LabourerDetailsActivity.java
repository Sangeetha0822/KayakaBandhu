package com.android.project.activitycontrollers.pradhan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.android.project.model.QRCode;
import com.android.project.utility.AppInstance;
import com.android.project.utility.Constants;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import com.android.project.utility.QRCodeHelper;
import com.google.gson.Gson;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class LabourerDetailsActivity extends AppCompatActivity {
    TextView nameTV, aadharTV, bplTV, voterTV, genderTV, ageTV, mobileTV,panchayathTV;
    ImageView iconIV;

    private long labourerID;
    private Labourer labourer = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labourerdetails);
        initializeUIComponents();
        labourerID = getIntent().getLongExtra(Constants.ID_KEY, 0);
        displayData();
    }

    public void initializeUIComponents() {
        nameTV = findViewById(R.id.name);
        mobileTV = findViewById(R.id.mobile);
        bplTV = findViewById(R.id.bpl);
        aadharTV = findViewById(R.id.aadhar);
        voterTV = findViewById(R.id.voter);
        genderTV = findViewById(R.id.gender);
        ageTV = findViewById(R.id.age);
        mobileTV = findViewById(R.id.mobile);
        panchayathTV = findViewById(R.id.panchayath);
        iconIV =  findViewById(R.id.image);

    }

    public void displayData()
    {
        AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);

        long labourerID = getIntent().getLongExtra(Constants.ID_KEY,0);
        labourer = databaseHelper.getLabourerWithID(labourerID);
        nameTV.setText(labourer.getName());
        bplTV.setText(labourer.getBplCardNumber());
        aadharTV.setText(labourer.getAadharID());
        voterTV.setText(labourer.getVoterID());
        genderTV.setText(labourer.getGender());
        ageTV.setText(String.valueOf(labourer.getAge()));
        mobileTV.setText(labourer.getMobile());
        panchayathTV.setText(labourer.getGramPanchayath());
        bplTV.setText(labourer.getBplCardNumber());
        aadharTV.setText(labourer.getAadharID());
        voterTV.setText(labourer.getVoterID());

        String imagePath = labourer.getProfilePath();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        if (null != bitmap) {
            iconIV.setImageBitmap(bitmap);
        }
        else
        {
            int resID = getResources().getIdentifier("noimage", Constants.DRAWABLE_RESOURCE, getPackageName());
            iconIV.setImageResource(resID);
        }

    }

    public void issueJobCard(View view) {
        if (labourer.isJobCardIssued() == true)
        {
            Toast.makeText(getApplicationContext(), Constants.JOB_CARD_IS_ALREADY_ISSUED, Toast.LENGTH_LONG).show();
        }
        else {
            AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
            labourer.setJobCardIssued(true);
            databaseHelper.updateLabourerJobCardStatus(labourer);
            Toast.makeText(getApplicationContext(), Constants.JOBCARD_ISSUED_SUCCESSFULLY, Toast.LENGTH_SHORT).show();

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
                sendSms(labourer.getMobile(), "Pradhan has issued Job Card using Kayaka Bandhu App");
                // Permission has already been granted
            }
            finish();
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
                    sendSms(labourer.getMobile(), "Pradhan has issued Job Card using Kayaka Bandhu App");
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