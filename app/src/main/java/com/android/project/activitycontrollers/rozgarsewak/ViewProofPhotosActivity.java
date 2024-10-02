package com.android.project.activitycontrollers.rozgarsewak;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.project.R;
import com.android.project.activitycontrollers.UserSelectionActivity;
import com.android.project.activitycontrollers.pradhan.AdminChangePasswordActivity;
import com.android.project.database.AppDatabaseHelper;
import com.android.project.utility.AppInstance;
import com.android.project.utility.Constants;

import java.util.ArrayList;
import java.util.Calendar;

public class ViewProofPhotosActivity extends AppCompatActivity {

    private ImageView beforeIV, afterIV;
    private Button proceedBT;
    private LinearLayout proofLL;
    private TextView noProofTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewproofphotos);
        initializeUIComponents();
        displayData();
    }

    public void initializeUIComponents()
    {
        beforeIV = findViewById(R.id.before);
        afterIV = findViewById(R.id.after);
        proceedBT = findViewById(R.id.proceed);
        proofLL = findViewById(R.id.proof_layout);
        noProofTV = findViewById(R.id.no_proof);

    }

    public void displayData()
    {
        long labourerID = getIntent().getLongExtra(Constants.ID_KEY, 0);
        AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
        ArrayList<String> proofPhotos = databaseHelper.getProofPhotosForLabourerForDate(labourerID, Calendar.getInstance());
        if (proofPhotos == null)
        {
            proceedBT.setVisibility(View.GONE);
            noProofTV.setVisibility(View.VISIBLE);
            proofLL.setVisibility(View.GONE);
        }
        else
        {
            proceedBT.setVisibility(View.VISIBLE);
            noProofTV.setVisibility(View.GONE);
            proofLL.setVisibility(View.VISIBLE);

            String beforePath = proofPhotos.get(0);
            Bitmap beforeBitmap = BitmapFactory.decodeFile(beforePath);
            if (null != beforeBitmap) {
                beforeIV.setImageBitmap(beforeBitmap);
            }
            else
            {
                int resID = getResources().getIdentifier("noimage", Constants.DRAWABLE_RESOURCE, getPackageName());
                beforeIV.setImageResource(resID);
            }
            String afterPath = proofPhotos.get(1);
            Bitmap afterBitmap = BitmapFactory.decodeFile(afterPath);
            if (null != afterBitmap) {
                afterIV.setImageBitmap(afterBitmap);
            }
            else
            {
                int resID = getResources().getIdentifier("noimage", Constants.DRAWABLE_RESOURCE, getPackageName());
                afterIV.setImageResource(resID);
            }

        }

    }

    public void proceed(View view)
    {
        Intent intent = new Intent(getApplicationContext(), PaymentDetailsActivity.class);
        long labourerID = getIntent().getLongExtra(Constants.ID_KEY, 0);
        intent.putExtra(Constants.ID_KEY, labourerID);
        startActivity(intent);
        finish();

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
