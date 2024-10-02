package com.android.project.activitycontrollers.pradhan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class ApproveLabourerListActivity extends AppCompatActivity {
    private ListView listView;
    private LabourerListItemAdapter customAdapter;
    private ArrayList<Labourer> labourerList;
    private TextView mNoLabourersView;
    private AppDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewlabourerlist);

        listView = findViewById(R.id.listView);
        mNoLabourersView = findViewById(R.id.no_labourer_text);
        mNoLabourersView.setText(Constants.NO_LABOURERS_REGISTERED_FOR_SCHEME_DESCRIPTION);

        databaseHelper = new AppDatabaseHelper(this);
        populateListView();
    }

    private void populateListView() {
        labourerList = databaseHelper.getRegisteredLabourerList();

        if (labourerList.size() > 0) {
            mNoLabourersView.setVisibility(View.GONE);
            customAdapter = new LabourerListItemAdapter(this, R.layout.labourerlist_item);
            customAdapter.setLabourerList(labourerList);
            listView.setAdapter(customAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Labourer labourer = labourerList.get(position);
                    Intent intent = new Intent(getApplicationContext(), ApproveLabourerActivity.class);
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

    private void reloadData() {
        labourerList.clear();
        labourerList.addAll(databaseHelper.getRegisteredLabourerList());

        if (labourerList.size() <= 0) {
            mNoLabourersView.setVisibility(View.VISIBLE);
        } else {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
