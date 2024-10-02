package com.android.project.activitycontrollers.labourer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.project.R;
import com.android.project.activitycontrollers.UserSelectionActivity;
import com.android.project.database.AppDatabaseHelper;
import com.android.project.model.Job;
import com.android.project.model.Labourer;
import com.android.project.model.QRCode;
import com.android.project.utility.AppInstance;
import com.android.project.utility.Constants;
import com.google.gson.Gson;

public class ViewJobCardActivity extends AppCompatActivity {
    TextView nameTV, aadharTV, bplTV, voterTV, genderTV, ageTV, mobileTV,panchayathTV,jobAllottedTV,bankTV, ifscTV, accountTV;
    ImageView iconIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewjobcard);
        initializeUIComponents();
        displayData();
    }

    public void initializeUIComponents() {
        nameTV = findViewById(R.id.name);
        bplTV = findViewById(R.id.bpl);
        aadharTV = findViewById(R.id.aadhar);
        voterTV = findViewById(R.id.voter);
        genderTV = findViewById(R.id.gender);
        ageTV = findViewById(R.id.age);
        mobileTV = findViewById(R.id.mobile);
        panchayathTV = findViewById(R.id.panchayath);
        jobAllottedTV = findViewById(R.id.job);
        bankTV = findViewById(R.id.bank);
        ifscTV = findViewById(R.id.ifsc);
        accountTV = findViewById(R.id.account);

        iconIV =  findViewById(R.id.image);
    }

    public void displayData()
    {

        AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
        Labourer labourer = ((AppInstance)getApplicationContext()).getCurrentLabourer();
        nameTV.setText(labourer.getName());
        bplTV.setText(labourer.getBplCardNumber());
        aadharTV.setText(labourer.getAadharID());
        voterTV.setText(labourer.getVoterID());
        genderTV.setText(labourer.getGender());
        ageTV.setText(String.valueOf(labourer.getAge()));
        mobileTV.setText(labourer.getMobile());
        panchayathTV.setText(labourer.getGramPanchayath());
        bankTV.setText(labourer.getBank());
        ifscTV.setText(labourer.getIfsc());
        accountTV.setText(labourer.getAccount());

        long jobID = databaseHelper.getJobIDForLabourer(labourer.getLabourerID());
        Job job = databaseHelper.getJobWithID(jobID);
        if (job == null)
        {
            jobAllottedTV.setText("No Job Allotted");
        }
        else {
            jobAllottedTV.setText(job.getTitle());
        }

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

    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sub_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case R.id.logout:
                ((AppInstance) getApplicationContext()).setCurrentLabourer(null);
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
