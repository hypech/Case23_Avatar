package com.hypech.case23_avatar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    final String TAG = "===> ";

    PopupWindow myPop;
    private static final int REQUEST_GALLERY= 0;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_CROP   = 3;
    private static final String IMAGE_FILE_NAME = "case23avatar.jpg";

    SharedPreferences mySP;
    SharedPreferences.Editor myEditor;

    Uri  mImageUri;
    View mView;
    ImageView mAvatar;
    String imageUriString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "on create restart");

        mySP     = getSharedPreferences("com.hypech.case23", 0);
        myEditor = mySP.edit();

        mAvatar = findViewById(R.id.main_icon);

        imageUriString = mySP.getString("cropURI", null);
        if (imageUriString !=null) {
            Uri imageUri = Uri.parse(imageUriString); //<-- parse
            mAvatar.setImageURI(imageUri);
        }

    }

    public void click_change_avatar(View v){

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width   = WindowManager.LayoutParams.MATCH_PARENT;
        int height  = WindowManager.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it

        myPop = new PopupWindow(mView, width, height, focusable);
        myPop.setAnimationStyle(R.style.popwindow_anim_style);
        myPop.setBackgroundDrawable(new ColorDrawable(0x0000000));

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        myPop.showAtLocation(mView, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                myPop.dismiss();
                return true;
            }
        });
    }

    public void click_picture(View v){
        // request permission for gallery
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        } else {
            myPop.dismiss();
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            // find the app to handle image
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_GALLERY);
            } else {
                Toast.makeText(MainActivity.this, "No App could open gallery.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void click_camera(View v){
        // request permission for camera
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
        } else {
            myPop.dismiss();
            imageCapture();
        }
    }

    public void click_cancel(View v){
        myPop.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // gallery
                case REQUEST_GALLERY:
                    try {
                        cropPic(data.getData());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    break;
                // camera
                case REQUEST_CAMERA:
                    File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                    Uri uri = getImageContentUri(MainActivity.this, temp);
                    cropPic(uri);
                case REQUEST_CROP:
                    Log.e(TAG,"call back");
                    Bitmap bitmap = BitmapFactory.decodeFile(mImageUri.getEncodedPath());
                    mAvatar.setImageBitmap(bitmap);
                    break;
            }
        }
    }

    /**
     * call back results
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    myPop.dismiss();
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_GALLERY);
                    // ????????????????????????????????? Intent ??? Activity
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, REQUEST_GALLERY);
                    } else {
                        Toast.makeText(MainActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myPop.dismiss();
                }
                break;
            case 300:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    myPop.dismiss();
                    imageCapture();
                } else {
                    myPop.dismiss();
                }
                break;
        }
    }

    /**
     * Camera
     */
    private void imageCapture() {
        File pictureFile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Uri pictureUri = FileProvider.getUriForFile(this,
                "hypech.com.fileProvider", pictureFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void cropPic(Uri uri) {
        // create folder
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String storage = Environment.getExternalStorageDirectory().getPath();
            File dirFile = new File(storage + "/cropAvatar");
            if (!dirFile.exists()) dirFile.mkdirs();
            File file = new File(dirFile, System.currentTimeMillis() + ".jpg");
            mImageUri = Uri.fromFile(file);

            myEditor.putString("cropURI", mImageUri.toString());
            myEditor.commit();

        }

        // start croping
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 600);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, REQUEST_CROP);
    }

    public Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
}