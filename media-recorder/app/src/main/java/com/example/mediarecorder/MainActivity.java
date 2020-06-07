package com.example.mediarecorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button record;
    private Button play;
    private Button stopRecording;
    private Button stopPlaying;
    private String pathInDevice;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        record = findViewById(R.id.record);
        play = findViewById(R.id.play);
        stopRecording = findViewById(R.id.stop_recording);
        stopPlaying = findViewById(R.id.stop_playing);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String fileName = "MPEG_" + timestamp + "_";
                    pathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName + "AudioRecording.3gp";

                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch  (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(MainActivity.this,"Recording starting", Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }
            }
        });

        stopRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                Toast.makeText(MainActivity.this, "Recording completed", Toast.LENGTH_LONG).show();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException, SecurityException, IllegalStateException {
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(pathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                Toast.makeText(MainActivity.this, "Recording Playing", Toast.LENGTH_SHORT).show();
            }
        });

        stopPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                }
            }
        });
    }

    private void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathInDevice);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(MainActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
    }

}

