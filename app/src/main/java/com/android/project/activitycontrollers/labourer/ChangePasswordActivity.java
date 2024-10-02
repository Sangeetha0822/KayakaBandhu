package com.android.project.activitycontrollers.labourer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.project.R;
import com.android.project.activitycontrollers.UserSelectionActivity;
import com.android.project.database.AppDatabaseHelper;
import com.android.project.model.Labourer;
import com.android.project.utility.AppInstance;
import com.android.project.utility.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText oldPasswordET, newPasswordET,  confirmNewPasswordET;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        initializeUIComponents();
    }

    public void initializeUIComponents()
    {
        oldPasswordET = findViewById(R.id.oldPassword);
        newPasswordET =  findViewById(R.id.newPassword);
        confirmNewPasswordET =  findViewById(R.id.confirmNewPassword);


    }

    public void changePassword(View view)
    {
        String oldPassword = oldPasswordET.getText().toString().trim();
        String newPassword = newPasswordET.getText().toString().trim();
        String confirmNewPassword = confirmNewPasswordET.getText().toString().trim();



        Labourer labourer = ((AppInstance)getApplicationContext()).getCurrentLabourer();

        if (oldPassword.length() == 0) {
            oldPasswordET.setError(Constants.MISSING_OLD_PASSWORD);
            oldPasswordET.requestFocus();
        }
        else if (false == labourer.getPassword().equals(oldPassword))
        {
            oldPasswordET.setError(Constants.OLD_PASSWORD_INCORRECT);
            oldPasswordET.requestFocus();
        }
        else if(newPassword.length() == 0)
        {
            newPasswordET.setError(Constants.MISSING_NEW_PASSWORD);
            newPasswordET.requestFocus();
        }
        else if(newPassword.length()< Constants.MINIMUM_PASSWORD_LENGTH )
        {
            newPasswordET.setError(Constants.INVALID_PASSWORD);
            newPasswordET.requestFocus();
        }
        else if (false == isValidPassword(newPassword) )
        {
            newPasswordET.setError(Constants.INVALID_PASSWORD);
            newPasswordET.requestFocus();
        }
        else if (confirmNewPassword.length() == 0) {
            confirmNewPasswordET.setError(Constants.MISSING_PASSWORD_CONFIRMATION);
            confirmNewPasswordET.requestFocus();
        }
        else if (!newPassword.equals(confirmNewPassword)) {
            confirmNewPasswordET.setError(Constants.PASSWORD_MISMATCH);
            confirmNewPasswordET.requestFocus();
        }
        else
        {
            AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
            databaseHelper.updatePasswordForLabourer(newPassword, labourer.getLabourerID());
            Toast.makeText(this, Constants.PASSWORD_CHANGED_SUCCESSFULLY, Toast.LENGTH_LONG).show();
            finish();

        }
    }

    public  boolean isValidPassword(final String password)
    {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[*@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                ((AppInstance) getApplicationContext()).setCurrentLabourer(null);
                Intent i = new Intent(this, UserSelectionActivity.class);
                startActivity(i);
                finish();
                return true;
        }

        return false;
    }

}
