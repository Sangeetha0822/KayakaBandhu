package com.android.project.activitycontrollers.rozgarsewak;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.project.R;
import com.android.project.activitycontrollers.UserSelectionActivity;
import com.android.project.database.AppDatabaseHelper;
import com.android.project.model.Labourer;
import com.android.project.utility.AppInstance;
import com.android.project.utility.Constants;
import com.android.project.utility.ImageFilePath;
import com.android.project.utility.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AddLabourerActivity extends AppCompatActivity {
    private EditText nameET, usernameET, passwordET, mobileET, ageET, panchayathET,bankET,ifscET, accountET;
    private ImageView iconIV;
    private String realPath =null;
    RadioGroup genderRG;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addlabourer);
        initializeUIComponents();
    }

    public void initializeUIComponents()
    {
        mobileET = findViewById(R.id.mobile);
        nameET =findViewById(R.id.name);
        iconIV=findViewById(R.id.image);
        usernameET =  findViewById(R.id.userName);
        passwordET = findViewById(R.id.password);
        ageET = findViewById(R.id.age);
        genderRG = findViewById(R.id.gender);
        panchayathET = findViewById(R.id.panchayath);
        bankET = findViewById(R.id.bank);
        ifscET = findViewById(R.id.ifsc);
        accountET = findViewById(R.id.account);

    }

    public void register(View view) {
        String username = usernameET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String name = nameET.getText().toString().trim();
        String mobile = mobileET.getText().toString().trim();

        String age = ageET.getText().toString().trim();
        String panchayath = panchayathET.getText().toString().trim();
        String bank = bankET.getText().toString().trim();
        String ifsc = ifscET.getText().toString().trim();
        String account = accountET.getText().toString().trim();

        AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
        int selectedGender = genderRG.getCheckedRadioButtonId();
        String userGender = null;
        if (selectedGender == R.id.male)
        {
            userGender = Constants.MALE;
        }
        else
        {
            userGender = Constants.FEMALE;
        }

        ArrayList<String> userNames = databaseHelper.getAllLabourerUserNames();

        if (name.length() == 0) {
            nameET.setError(Constants.MISSING_NAME);
            nameET.requestFocus();
        } else if (mobile.length() == 0) {
            mobileET.setError(Constants.MISSING_MOBILE);
            mobileET.requestFocus();
        } else if (mobile.length() != 10) {
            mobileET.setError(Constants.INVALID_MOBILE);
            mobileET.requestFocus();
        } else if (username.length() == 0) {
            usernameET.setError(Constants.MISSING_USERNAME);
            usernameET.requestFocus();
        } else if (userNames.size() > 0 && userNames.contains(username)) {
            usernameET.setError(Constants.DUPLICATE_USERNAME);
            usernameET.requestFocus();
        } else if (panchayath.length() == 0) {
            panchayathET.setError(Constants.MISSING_PANCHAYATH);
            panchayathET.requestFocus();
        } else if (realPath == null) {
            Toast.makeText(getApplicationContext(), Constants.MISSING_PROFILEPHOTO, Toast.LENGTH_LONG).show();
        }
        else if (bank.length()==0) {
            Toast.makeText(getApplicationContext(), Constants.MISSING_BANK, Toast.LENGTH_LONG).show();
        }else if (ifsc.length()==0) {
            Toast.makeText(getApplicationContext(), Constants.MISSING_IFSC, Toast.LENGTH_LONG).show();
        }else if (account.length()==0) {
            Toast.makeText(getApplicationContext(), Constants.MISSING_ACCOUNT, Toast.LENGTH_LONG).show();
        }
        else {
            Labourer labourer = new Labourer();
            labourer.setName(name);
            labourer.setMobile(mobile);
            labourer.setUsername(username);
            labourer.setPassword(password);
            labourer.setAge(Integer.parseInt(age));
            labourer.setProfilePath(realPath);
            labourer.setGramPanchayath(panchayath);
            labourer.setGender(userGender);
            labourer.setBank(bank);
            labourer.setIfsc(ifsc);
            labourer.setAccount(account);
            labourer.setApproved(false);

            int labourID = databaseHelper.addLabourer(labourer);
            labourer.setLabourerID(labourID);

            Toast.makeText(this, Constants.LABOURER_ADDED_SUCCESSFULLY, Toast.LENGTH_LONG).show();
            clearUI();
        }
    }

    public void clearUI()
    {
        nameET.setText("");
        mobileET.setText("");
        usernameET.setText("");
        passwordET.setText("");
        ageET.setText("");

        realPath = null;
        panchayathET.setText("");
        passwordET.setText("");
        iconIV.setVisibility(View.GONE);
        bankET.setText("");
        ifscET.setText("");
        accountET.setText("");
    }
    public void selectPhoto(View view) {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                boolean result = Utility.checkPermission(AddLabourerActivity.this);
                if (items[item].equals("Take Photo")) {
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constants.REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), Constants.SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == Constants.REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Uri uri = data.getData();


        realPath = ImageFilePath.getPath(this, data.getData());

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

            iconIV.setImageBitmap(bitmap);
            iconIV.setVisibility(View.VISIBLE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            Uri fileUri = Uri.fromFile(destination);
            realPath = ImageFilePath.getPath(this, fileUri);
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        iconIV.setImageBitmap(thumbnail);
        iconIV.setVisibility(View.VISIBLE);
    }

    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sub_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected (MenuItem item){
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

