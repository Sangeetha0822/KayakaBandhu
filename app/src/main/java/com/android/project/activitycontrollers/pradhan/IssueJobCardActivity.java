package com.android.project.activitycontrollers.pradhan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.project.R;
import com.android.project.activitycontrollers.UserSelectionActivity;
import com.android.project.adapters.LabourerListItemAdapter;
import com.android.project.database.AppDatabaseHelper;
import com.android.project.model.Labourer;
import com.android.project.utility.AppInstance;
import com.android.project.utility.Constants;

import java.util.ArrayList;

public class IssueJobCardActivity extends AppCompatActivity {
    private ListView listView = null;

    LabourerListItemAdapter customAdapter;
    ArrayList<Labourer> labourerList;
    private TextView mNoLabourersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewlabourerlist);
        populateListView();
    }

    public void populateListView()
    {
        mNoLabourersView = findViewById(R.id.no_labourer_text);
        mNoLabourersView.setText(Constants.NO_LABOURERS_REGISTERED_FOR_SCHEME_DESCRIPTION);

        final AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);

        labourerList = databaseHelper.getLabourersRegisteredForTheScheme();
        if (labourerList.size() > 0) {
            mNoLabourersView.setVisibility(View.GONE);
            customAdapter = new LabourerListItemAdapter(this, R.layout.labourerlist_item);
            customAdapter.setLabourerList(labourerList);
            listView = findViewById(R.id.listView);
            listView.setAdapter(customAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    Labourer labourer = labourerList.get(i);
                    Intent intent = new Intent(getApplicationContext(), LabourerDetailsActivity.class);

                    intent.putExtra(Constants.ID_KEY, labourer.getLabourerID());
                    startActivity(intent);
                }
            });


        } else {
            mNoLabourersView.setVisibility(View.VISIBLE);
            if (customAdapter != null) {
                reloadData();
            }
        }
    }

    public void reloadData() {
        labourerList.clear();
        AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
        labourerList = databaseHelper.getLabourersRegisteredForTheScheme();
        if (labourerList.size() <= 0) {
            mNoLabourersView.setVisibility(View.VISIBLE);
        }
        else {
            mNoLabourersView.setVisibility(View.GONE);
            customAdapter.setLabourerList(labourerList);
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
