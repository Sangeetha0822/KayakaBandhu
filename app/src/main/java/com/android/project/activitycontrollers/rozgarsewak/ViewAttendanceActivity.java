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
import com.android.project.adapters.ViewAttendanceListItemAdapter;
import com.android.project.database.AppDatabaseHelper;
import com.android.project.model.Attendance;
import com.android.project.utility.AppInstance;
import com.android.project.utility.Constants;

import java.util.ArrayList;

public class ViewAttendanceActivity extends AppCompatActivity {
    private ListView listView = null;

    ViewAttendanceListItemAdapter customAdapter;
    ArrayList<Attendance> attendanceList;
    private TextView mNoAttendanceView;
    long labourerID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewattendancelist);
        populateListView();
    }

    public void populateListView() {
        mNoAttendanceView = findViewById(R.id.no_attendance_text);
        mNoAttendanceView.setText(Constants.NO_ATTENDANCE_DESCRIPTION);

        final AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);

        labourerID = getIntent().getLongExtra(Constants.ID_KEY, 0);

        attendanceList = databaseHelper.getAttendanceListForLabourer(labourerID);
        if (attendanceList.size() > 0) {
            mNoAttendanceView.setVisibility(View.GONE);
            customAdapter = new ViewAttendanceListItemAdapter(this, R.layout.viewattendancelist_item);
            customAdapter.setAttendanceList(attendanceList);
            listView = findViewById(R.id.listView);
            listView.setAdapter(customAdapter);

        } else {
            mNoAttendanceView.setVisibility(View.VISIBLE);
            if (customAdapter != null) {
                reloadData();
            }
        }
    }

    public void reloadData() {
        attendanceList.clear();
        AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
        attendanceList = databaseHelper.getAttendanceListForLabourer(labourerID);
        if (attendanceList.size() <= 0) {
            mNoAttendanceView.setVisibility(View.VISIBLE);
        } else {
            mNoAttendanceView.setVisibility(View.GONE);
            customAdapter.setAttendanceList(attendanceList);
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