package com.android.project.activitycontrollers.pradhan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.project.R;
import com.android.project.activitycontrollers.UserSelectionActivity;
import com.android.project.utility.AppInstance;
import com.android.project.utility.Constants;

import java.util.ArrayList;


public class PradhanHomeActivity extends AppCompatActivity {
    private RadioGroup optionsRG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pradhan_homescreen);
        initializeUIComponents();
    }

    public void initializeUIComponents()
    {
        optionsRG = findViewById(R.id.options);
        optionsRG.check(R.id.approvelabourer);
    }

    public void next(View view)
    {
        int selectedOption = optionsRG.getCheckedRadioButtonId();
        Intent intent = null;
        if (selectedOption == R.id.approvelabourer)
        {
            intent = new Intent(getApplicationContext(),ApproveLabourerListActivity.class);
        }
        else if(selectedOption==R.id.issuejobcard)
        {
            intent = new Intent(getApplicationContext(),IssueJobCardActivity.class);
        }
        else if(selectedOption==R.id.assignSewak)
        {
            intent = new Intent(getApplicationContext(),AddRozgarSewakActivity.class);
        }
        else if(selectedOption==R.id.addjob)
        {
            intent = new Intent(getApplicationContext(),AddJobActivity.class);
        }
        startActivity(intent);

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
