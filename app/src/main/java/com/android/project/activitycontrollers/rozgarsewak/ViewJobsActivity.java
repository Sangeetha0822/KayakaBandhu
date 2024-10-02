package com.android.project.activitycontrollers.rozgarsewak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.project.R;
import com.android.project.activitycontrollers.UserSelectionActivity;
import com.android.project.activitycontrollers.pradhan.AdminChangePasswordActivity;
import com.android.project.adapters.JobListItemAdapter;
import com.android.project.database.AppDatabaseHelper;
import com.android.project.model.Job;
import com.android.project.utility.AppInstance;
import com.android.project.utility.Constants;

import java.util.ArrayList;

public class ViewJobsActivity extends AppCompatActivity {
    private ListView listView = null;

    JobListItemAdapter customAdapter;
    ArrayList<Job> jobList;
    private TextView mNoJobsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewjoblist);
        populateListView();
    }

    public void populateListView() {
        mNoJobsView = findViewById(R.id.no_job_text);
        mNoJobsView.setText(Constants.NO_JOBS_DESCRIPTION);

        AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);

        jobList = databaseHelper.getJobList();
        if (jobList.size() > 0) {
            mNoJobsView.setVisibility(View.GONE);
            customAdapter = new JobListItemAdapter(this, R.layout.joblist_item);
            customAdapter.setJobList(jobList);
            listView = findViewById(R.id.listView);
            listView.setAdapter(customAdapter);

        } else {
            mNoJobsView.setVisibility(View.VISIBLE);
            if (customAdapter != null) {
                reloadData();
            }
        }
    }

    public void reloadData() {
        jobList.clear();
        AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
        jobList = databaseHelper.getJobList();
        if (jobList.size() <= 0) {
            mNoJobsView.setVisibility(View.VISIBLE);
        } else {
            mNoJobsView.setVisibility(View.GONE);
            customAdapter.setJobList(jobList);
            customAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reloadData();
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
}

