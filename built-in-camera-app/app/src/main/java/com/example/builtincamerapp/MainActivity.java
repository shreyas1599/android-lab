package com.example.builtincamerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button imageButton;
    private Button videoButton;
    private ImageView mImageView;
    private VideoView mVideoView;
    private Button startButton;
    private Uri videoFileUrl;
    String mCurrentPhotoPath;

    private static final int REQUEST_IMAGE = 100;
    private static final int FILE_PERM = 12;
    private static final int REQUEST_TAKE_PHOTO = 1;
    public static int VIDEO_CAPTURED = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButton = findViewById(R.id.image_capture);
        videoButton = findViewById(R.id.video_capture);
        mImageView = findViewById(R.id.camera_image);
        mVideoView = findViewById(R.id.camera_video);
        startButton = findViewById(R.id.play_button);
        mImageView.setVisibility(View.GONE);
        mVideoView.setVisibility(View.GONE);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVideoView.setVisibility(View.GONE);
//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                    startActivityForResult(takePictureIntent, REQUEST_IMAGE);
//                }
                dispatchTakePictureIntent();

            }
        });

        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageView.setVisibility(View.GONE);
//                Intent captureVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                startActivityForResult(captureVideoIntent, VIDEO_CAPTURED);
                dispatchTakeVideoIntent();

            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVideoView.setVideoURI(videoFileUrl);
                mVideoView.start();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            mImageView.setVisibility(View.VISIBLE);
            File imgFile = new File(mCurrentPhotoPath);
            Log.d(imgFile.toString(), "asasasasas");
            if (imgFile.exists()) {
                Bitmap imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                mImageView.setImageBitmap(imageBitmap);
            }
        }
        if (requestCode == 2) {
            mVideoView.setVisibility(View.VISIBLE);
            videoFileUrl = data.getData();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFilename = "JPEG_" + timeStamp + "_";

        File storageDir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        if(!storageDir.exists()) {
            storageDir.mkdir();
        }

        File image = File.createTempFile(
                imageFilename,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private File createVideoFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFilename = "MPEG_" + timeStamp + "_";

        File storageDir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        if(!storageDir.exists()) {
            storageDir.mkdir();
        }

        File video = File.createTempFile(
                imageFilename,
                ".mp4",
                storageDir
        );
        mCurrentPhotoPath = video.getAbsolutePath();
        return video;
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, FILE_PERM);
            }
        } else {
            if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createVideoFile();
                } catch (IOException ex) {
                    Log.d(ex.toString(), "he");
                }

                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this, "com.example.builtincamerapp.fileprovider", photoFile);

                    takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                    startActivityForResult(takeVideoIntent, VIDEO_CAPTURED);
                    Log.d(photoURI.toString(), "mama");
                }
            }
        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, FILE_PERM);
            }
        } else {
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.d(ex.toString(), "hello");
                }

                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this, "com.example.builtincamerapp.fileprovider", photoFile);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    Log.d(photoURI.toString(), "mama");
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case FILE_PERM: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                    Toast.makeText(getApplicationContext(), "SMS sent", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "SMS failed", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }
}
