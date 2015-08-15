package com.example.ayoolasolomon.myruns;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

  public static String APP_TAG = "MyRuns";
  public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 111;
  public final static int SELECT_FILE = 112;

  private File output = null;
  public String photoFileName = "photo.jpg";

  public static final String MY_PREFEREANCES = "Profile";
  public static final String Name = "nmaeKey", Phone = "phoneKey", Email = "emailkey", Class_year = "classKey", Major = "majorKey", Gender = "genderKey";
  SharedPreferences sharedPreferences;

  private TextView mName, mPhone, mEmail, mClass, mMajor;
  private RadioGroup mGender;
  private ImageView mProfileImage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.profile_activity);

    sharedPreferences = this.getSharedPreferences(MY_PREFEREANCES, Context.MODE_PRIVATE);
    String name = sharedPreferences.getString(Name, "");
    String phone = sharedPreferences.getString(Phone, "");
    String email = sharedPreferences.getString(Email, "");
    String class_year = sharedPreferences.getString(Class_year, "");
    String major = sharedPreferences.getString(Major, "");
    int gender = sharedPreferences.getInt(Gender, -1);
    String profilePicture = sharedPreferences.getString("ImagePath", "");

    mName = (TextView)findViewById(R.id.name);
    mPhone = (TextView)findViewById(R.id.phone_number);
    mEmail = (TextView)findViewById(R.id.email_address);
    mClass = (TextView)findViewById(R.id.class_year);
    mMajor = (TextView)findViewById(R.id.major);
    RadioButton radioButton = (RadioButton) ((RadioGroup) findViewById(R.id.gender)).getChildAt(gender);

    mName.setText(name);
    mPhone.setText(phone);
    mEmail.setText(email);
    mClass.setText(class_year);
    mMajor.setText(major);

    try {
      radioButton.setChecked(true);
    } catch (Exception e) {
      e.printStackTrace();
    }

    loadSnap(profilePicture);

  }

  public void selectImage(View view) {

    startDialog();

  }

  public void startCamera() {
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    output = new File(dir, "ProfilePicture.jpeg");
    intent.putExtra(MediaStore.EXTRA_OUTPUT, getProfilePhotoUri(photoFileName));

    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
  }

  public void chooseFromLibrary() {

    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    intent.setType("image/*");
    startActivityForResult(Intent.createChooser(intent, "Choose Profile Picture"), SELECT_FILE);

  }

  public void startDialog() {

    final String[] items = { "Take Photo", "Choose from Library" };

    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
    builder.setTitle("Profile Picture");
    builder.setItems(items, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int item) {
        if (items[item].equals(items[0])) {
          startCamera();
        } else if (items[item].equals(items[1])) {
          chooseFromLibrary();
        }
      }
    });
    builder.show();
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (resultCode == RESULT_OK) {
      if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

        handleImageFromCamera();

      } else if (requestCode == SELECT_FILE) {

        handleImageFromGallery(data);
      }
    } else {
      Toast.makeText(this, "Picture wasn't taken!",Toast.LENGTH_SHORT).show();
    }
  }

  public void handleImageFromCamera() {

    Uri takenProfilePicture = getProfilePhotoUri(photoFileName);

    Bitmap rotatedImage = rotateBitmapOrientation(takenProfilePicture.getPath());
    mProfileImage = (ImageView) findViewById(R.id.profile_picture);
    mProfileImage.setImageBitmap(rotatedImage);

  }

  public void handleImageFromGallery(Intent data) {

    Uri selectedImageUri = data.getData();

    String[] proj = new String[] { android.provider.MediaStore.Images.ImageColumns.DATA };

    Cursor cursor = getContentResolver().query(selectedImageUri, proj, null,
        null, null);
    int column_index = cursor
        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    cursor.moveToFirst();

    String filename = cursor.getString(column_index);
    Bitmap bitmap;

    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(filename, options);
    final int REQUIRED_SIZE = 200;
    int scale = 1;
    while (options.outWidth / scale / 2 >= REQUIRED_SIZE
        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
      scale *= 2;
    options.inSampleSize = scale;
    options.inJustDecodeBounds = false;
    bitmap = BitmapFactory.decodeFile(filename, options);

    mProfileImage = (ImageView) findViewById(R.id.profile_picture);
    mProfileImage.setImageBitmap(bitmap);

    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString("ImagePath", filename);
    editor.apply();
  }

  public Bitmap rotateBitmapOrientation(String photoFilePath) {
    // Create and configure BitmapFactory
    BitmapFactory.Options bounds = new BitmapFactory.Options();
    bounds.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(photoFilePath, bounds);
    BitmapFactory.Options opts = new BitmapFactory.Options();
    Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
    // Read EXIF Data
    ExifInterface exif = null;
    try {
      exif = new ExifInterface(photoFilePath);
    } catch (IOException e) {
      e.printStackTrace();
    }
    String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
    int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
    int rotationAngle = 0;
    if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
    if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
    if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
    // Rotate Bitmap
    Matrix matrix = new Matrix();
    matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
    Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
    saveSnap();

    // Return result
    return rotatedBitmap;
  }

  public Uri getProfilePhotoUri(String fileName) {
    if (isExternalStorageAvailable()) {
      File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), APP_TAG);

      if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
        Log.d(APP_TAG, "failed to create directory");
      }

      return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
    }

    return null;
  }

  private boolean isExternalStorageAvailable() {
    String state = Environment.getExternalStorageState();
    if (state.equals(Environment.MEDIA_MOUNTED)) {
      return true;
    }

    return false;
  }

  public void saveProfile(View view) {

    SharedPreferences.Editor editor = sharedPreferences.edit();

    mName = (TextView)findViewById(R.id.name);
    String name = mName.getText().toString();

    mPhone = (TextView)findViewById(R.id.phone_number);
    String phone = mPhone.getText().toString();

    mEmail = (TextView)findViewById(R.id.email_address);
    String email = mEmail.getText().toString();

    mClass = (TextView)findViewById(R.id.class_year);
    String class_year = mClass.getText().toString();

    mMajor = (TextView)findViewById(R.id.major);
    String major = mMajor.getText().toString();

    mGender = (RadioGroup)findViewById(R.id.gender);
    int gender = mGender.indexOfChild(findViewById(mGender.getCheckedRadioButtonId()));

    editor.putString(Name, name);
    editor.putString(Phone, phone);
    editor.putString(Email, email);
    editor.putString(Major, major);
    editor.putInt(Gender, gender);
    editor.putString(Class_year, class_year);
    editor.apply();

    Toast.makeText(this, "Profile Successfully Saved", Toast.LENGTH_SHORT).show();
    finish();
  }

  public void cancelEdit(View view) {

    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
    finish();
  }

  private void saveSnap() {

    mProfileImage = (ImageView) findViewById(R.id.profile_picture);
    mProfileImage.buildDrawingCache();
    Bitmap bmap = mProfileImage.getDrawingCache();
    try {
      FileOutputStream fos = openFileOutput(
          getString(R.string.profile_picture), MODE_PRIVATE);
      fos.flush();
      fos.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  private void loadSnap(String profilePicture) {

    if (profilePicture.isEmpty()) {
      try {
        FileInputStream fis = openFileInput(getString(R.string.profile_picture));
        Log.d("Try", "fis: " + fis);
        Bitmap bmap = BitmapFactory.decodeStream(fis);
        mProfileImage.setImageBitmap(bmap);
        fis.close();
      } catch (IOException e) {
        // Default profile photo if no photo saved before.
        e.printStackTrace();
      }
    } else {

      Bitmap bitmap;

      bitmap = BitmapFactory.decodeFile(profilePicture);

      mProfileImage = (ImageView) findViewById(R.id.profile_picture);
      mProfileImage.setImageBitmap(bitmap);
      mProfileImage = (ImageView) findViewById(R.id.profile_picture);
    }
  }
}
