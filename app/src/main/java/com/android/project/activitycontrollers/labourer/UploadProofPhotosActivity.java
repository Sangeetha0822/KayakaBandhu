package com.android.project.activitycontrollers.labourer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.project.R;
import com.android.project.activitycontrollers.UserSelectionActivity;
import com.android.project.activitycontrollers.pradhan.AddRozgarSewakActivity;
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
import java.util.Calendar;

public class UploadProofPhotosActivity extends AppCompatActivity {
    private ImageView beforeIV, afterIV;
    private String beforePath = null, afterPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadbeforeafterphotos);
        initializeUIComponents();

    }

    public void initializeUIComponents() {
        beforeIV = findViewById(R.id.before);
        afterIV = findViewById(R.id.after);
    }

    public void upload(View view) {

        if (beforePath == null) {
            Toast.makeText(getApplicationContext(), Constants.MISSING_BEFOREPHOTO, Toast.LENGTH_LONG).show();
        }
        else if (afterPath == null) {
            Toast.makeText(getApplicationContext(), Constants.MISSING_AFTERPHOTO, Toast.LENGTH_LONG).show();
        }
        else
        {
            Labourer labourer = ((AppInstance)getApplicationContext()).getCurrentLabourer();
            AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
            Calendar date = Calendar.getInstance();

            databaseHelper.updateProofPhotosForLabourer(labourer, beforePath, afterPath,date);

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(Constants.PROGRESSUPLOADING); // Setting Message
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);

            new Handler().postDelayed(new Runnable() {
                /*
                 * Showing progress dialog with a timer.
                 */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity

                    progressDialog.dismiss();
                    finish();
                    Toast.makeText(getApplicationContext(), Constants.PHOTOS_UPLOADED_SUCCESSFULLY, Toast.LENGTH_LONG).show();
                }
            }, 3000);
        }

    }


    public void selectBeforePhoto(View view) {
        showAlertFor(Constants.BEFORE);
    }

    public void selectAfterPhoto(View view) { showAlertFor(Constants.AFTER); }

    public void showAlertFor(final int status)
    {

    final CharSequence[] items = {"Take Photo", "Choose from Library",
            "Cancel"};
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int item) {

            boolean result = Utility.checkPermission(UploadProofPhotosActivity.this);
            if (items[item].equals("Take Photo")) {
                if (result) {
                    if (status == Constants.BEFORE) {
                        cameraIntent(Constants.REQUEST_CAMERA_FOR_BEFORE_PHOTO);
                    }
                    else {
                        cameraIntent(Constants.REQUEST_CAMERA_FOR_AFTER_PHOTO);

                    }
                }
            } else if (items[item].equals("Choose from Library")) {
                if (result) {
                    if (status == Constants.BEFORE) {
                        galleryIntent(Constants.SELECT_FILE_FOR_BEFORE_PHOTO);
                    }
                    else {
                        galleryIntent(Constants.SELECT_FILE_FOR_AFTER_PHOTO);

                    }
                }
            } else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        }
    });
        builder.show();}

    private void cameraIntent(int status) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, status);
    }

    private void galleryIntent(int status) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), status);
    }

    @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.SELECT_FILE_FOR_AFTER_PHOTO || requestCode == Constants.SELECT_FILE_FOR_BEFORE_PHOTO)
                onSelectFromGalleryResult(data, requestCode);
            else if (requestCode == Constants.REQUEST_CAMERA_FOR_BEFORE_PHOTO || requestCode == Constants.REQUEST_CAMERA_FOR_AFTER_PHOTO)
                onCaptureImageResult(data, requestCode);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data, int status) {

        Uri uri = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

        if (status == Constants.SELECT_FILE_FOR_BEFORE_PHOTO) {
            beforePath = ImageFilePath.getPath(this, data.getData());
            beforeIV.setImageBitmap(bitmap);
            beforeIV.setVisibility(View.VISIBLE);

        }
        else {
            afterPath = ImageFilePath.getPath(this, data.getData());
            afterIV.setImageBitmap(bitmap);
            afterIV.setVisibility(View.VISIBLE);
        }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void onCaptureImageResult(Intent data, int status) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            Uri fileUri = Uri.fromFile(destination);
            if (status == Constants.REQUEST_CAMERA_FOR_BEFORE_PHOTO) {
                beforePath = ImageFilePath.getPath(this, fileUri);

                beforeIV.setImageBitmap(thumbnail);
                beforeIV.setVisibility(View.VISIBLE);

            }
            else {
                afterPath = ImageFilePath.getPath(this, fileUri);
                afterIV.setImageBitmap(thumbnail);
                afterIV.setVisibility(View.VISIBLE);
            }

            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
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
                AlertDialog.Builder helpDialogBuilder = new AlertDialog.Builder(this);
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
