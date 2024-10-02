package com.android.project.activitycontrollers.labourer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.project.R;
import com.android.project.activitycontrollers.UserSelectionActivity;
import com.android.project.database.AppDatabaseHelper;
import com.android.project.model.Labourer;
import com.android.project.utility.Constants;
import com.android.project.utility.ImageFilePath;
import com.android.project.utility.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class LabourerSignupActivity extends AppCompatActivity {
        private EditText nameET, usernameET, passwordET, mobileET, ageET, panchayathET,bankET,ifscET, accountET;
        private ImageView iconIV;
        private String realPath =null;
        RadioGroup genderRG;


        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_labourer_registrationscreen);
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

            ArrayList<String> userNames = databaseHelper.getAllLabourerUserNames();
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
            } else if (password.length() == 0)
            {
                passwordET.setError(Constants.MISSING_PASSWORD);
                passwordET.requestFocus();
            }
            else if(password.length()<Constants.MINIMUM_PASSWORD_LENGTH)
            {
                passwordET.setError(Constants.INVALID_PASSWORD);
                passwordET.requestFocus();
            }

            else if (panchayath.length() == 0) {
                panchayathET.setError(Constants.MISSING_PANCHAYATH);
                panchayathET.requestFocus();
            }
            else if (bank.length()==0) {
                Toast.makeText(getApplicationContext(), Constants.MISSING_BANK, Toast.LENGTH_LONG).show();
            }else if (ifsc.length()==0) {
                Toast.makeText(getApplicationContext(), Constants.MISSING_IFSC, Toast.LENGTH_LONG).show();
            }else if (account.length()==0) {
                Toast.makeText(getApplicationContext(), Constants.MISSING_ACCOUNT, Toast.LENGTH_LONG).show();
            }
            else if (realPath == null) {
                Toast.makeText(getApplicationContext(), Constants.MISSING_PROFILEPHOTO, Toast.LENGTH_LONG).show();
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

                Toast.makeText(this, Constants.LABOURER_REGISTRATION_SUCCESSFULL, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(this, UserSelectionActivity.class);
                startActivity(intent);
            }
        }

    public void selectPhoto(View view) {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                boolean result = Utility.checkPermission(LabourerSignupActivity.this);
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
        intent.setAction(Intent.ACTION_GET_CONTENT);
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


    }

