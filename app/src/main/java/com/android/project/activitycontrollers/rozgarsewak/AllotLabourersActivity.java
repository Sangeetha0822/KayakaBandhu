package com.android.project.activitycontrollers.rozgarsewak;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.project.R;
import com.android.project.activitycontrollers.UserSelectionActivity;
import com.android.project.adapters.AllotLabourerListItemAdapter;
import com.android.project.database.AppDatabaseHelper;
import com.android.project.model.Job;
import com.android.project.model.Labourer;
import com.android.project.model.RozgarSewak;
import com.android.project.utility.AppInstance;
import com.android.project.utility.Constants;

import java.util.ArrayList;

public class AllotLabourersActivity extends AppCompatActivity {
    ArrayList<Job> jobList = null;

    private Spinner jobSP;
    ArrayList<String> jobTitles = null;


    private ListView listView = null;

    AllotLabourerListItemAdapter customAdapter;
    private TextView mNoLabourerView;
    ArrayList<Labourer> labourerList = null;
    Button updateButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allotlabourers);
        initializeUIComponents();
    }

    public void initializeUIComponents() {
        final AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
        jobSP = findViewById(R.id.joblist);
        updateButton = findViewById(R.id.button);
        jobList = databaseHelper.getJobList();
        jobTitles = new ArrayList<>();
        for (Job job : jobList) {
            jobTitles.add(job.getTitle());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, jobTitles);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobSP.setAdapter(dataAdapter);

        checkIfTimeElapsed();
        labourerList = databaseHelper.getLabourersIssuedWithJobCard();
        populateListView();




    }

    // To check if 12 hours elapsed. If yes, we are resetting the allotment status
    public void checkIfTimeElapsed()
    {
        AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
        ArrayList<Labourer> allottedLabourers = databaseHelper.getLabourersAllottedWithJob();
        SharedPreferences mypref = PreferenceManager.getDefaultSharedPreferences(this);

        for (Labourer labourer:allottedLabourers)
        {
            if(mypref.contains(String.valueOf(labourer.getLabourerID())))
            {
                // Check time elapsed
                if (System.currentTimeMillis() >= Long.parseLong(mypref.getString(String.valueOf(labourer.getLabourerID()),"")) + 12 * 60 * 60 * 1000) {
                    // time has elapsed
                    labourer.setAllottedForJob(false);
                    databaseHelper.updatedJobAllottedStatusForLabourer(labourer);
                    databaseHelper.removeLabourerFromJob(labourer);
                    mypref.edit().remove(String.valueOf(labourer.getLabourerID()));
                    mypref.edit().commit();

                }

            }
        }
    }


    public void populateListView() {
        mNoLabourerView = findViewById(R.id.no_labourer_text);
        mNoLabourerView.setText(Constants.NO_LABOURER_AVAILABLE_DESCRIPTION);

        if (labourerList.size() > 0) {
            mNoLabourerView.setVisibility(View.GONE);
            customAdapter = new AllotLabourerListItemAdapter(this, R.layout.allotlabourerlist_item);
            customAdapter.setLabourerList(labourerList);
            listView = findViewById(R.id.listView);
            listView.setAdapter(customAdapter);
            updateButton.setVisibility(View.VISIBLE);
        } else {
            mNoLabourerView.setVisibility(View.VISIBLE);
            updateButton.setVisibility(View.GONE);

            if (customAdapter != null) {
                reloadData();
            }
        }
    }

    public void reloadData() {
        labourerList.clear();
        customAdapter.setLabourerList(labourerList);
        customAdapter.notifyDataSetChanged();
    }

    public void update(View view) {

        if (jobList.size() > 0) {
            Job job = jobList.get(jobSP.getSelectedItemPosition());
            AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
            RozgarSewak rozgarSewak = ((AppInstance)getApplicationContext()).getCurrentRozgarSewak();

            CheckBox cb;
            for (int x = 0; x < listView.getChildCount(); x++) {
                cb = listView.getChildAt(x).findViewById(R.id.present);
                if (true == cb.isChecked()) {
                    databaseHelper.addLabourerToJob(labourerList.get(x).getLabourerID(), job.getJobID());
                    labourerList.get(x).setAllottedForJob(true);
                    databaseHelper.updatedJobAllottedStatusForLabourer(labourerList.get(x));

                    // Save current time - This is to save the time to shared preferences
                    long savedMillis = System.currentTimeMillis();
                    SharedPreferences mypref = PreferenceManager.getDefaultSharedPreferences(this);

                    SharedPreferences.Editor prefsEditr = mypref.edit();
                    prefsEditr.putString(String.valueOf(labourerList.get(x).getLabourerID()), String.valueOf(savedMillis));
                    prefsEditr.commit();

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
                        sendSms(labourerList.get(x).getMobile(), "Rozgar Sewak has allotted you to job: " + job.getTitle() + ".\n Contact for further details: " + rozgarSewak.getMobile());
                        // Permission has already been granted
                    }

                }
            }

            Toast.makeText(getApplicationContext(), Constants.LABOURERS_ALLOTTED_SUCCESSFULLY, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), Constants.NO_JOBS, Toast.LENGTH_LONG).show();
        }
        finish();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sub_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                ((AppInstance) getApplicationContext()).setCurrentRozgarSewak(null);
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
            case R.id.changePassword:
                Intent intent = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent);
                return true;
            case R.id.help:
                android.app.AlertDialog.Builder helpDialogBuilder = new android.app.AlertDialog.Builder(this);
                helpDialogBuilder.setIcon(R.drawable.applogo);
                helpDialogBuilder.setTitle(R.string.app_name);
                helpDialogBuilder.setMessage(Constants.HELP_MESSAGE);
                helpDialogBuilder.create();
                helpDialogBuilder.show();
                return true;
            case R.id.feedback:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, Constants.FEEDBACK_SUBJECT);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.FEEDBACK_MAILID});

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send feedback..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_LONG).show();
                }
                return true;

        }

        return false;
    }
}