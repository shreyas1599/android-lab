package com.example.smsmanagerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smsmanagerapp.AESHelper;

public class MainActivity extends AppCompatActivity {

    Button sendButton, openButton;
    EditText phoneNumber, smsBody;
    String phoneNo, message;
    private static final int MY_SMS_PERMISSION_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendButton = findViewById(R.id.sms_this_app);
        openButton = findViewById(R.id.sms_different_app);
        phoneNumber = findViewById(R.id.phone_number);
        smsBody = findViewById(R.id.sms_body);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNextActivity();
            }
        });
    }

    private void openNextActivity() {
        phoneNo = phoneNumber.getText().toString();
        message = smsBody.getText().toString();
        Intent smsIntent = new Intent(MainActivity.this, ReceivedSMS.class);
        smsIntent.setData(Uri.parse("sms:" + phoneNo));
        smsIntent.putExtra("sms_body", message);


        try {
            startActivity(smsIntent);
        } catch(android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void sendMessage() {
        phoneNo = phoneNumber.getText().toString();
        message = smsBody.getText().toString();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)){

            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, MY_SMS_PERMISSION_REQUEST);
            }
        } else {
            try {
                String enc = AESHelper.encrypt(message, "testseed");
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, enc, null, null);
                Toast.makeText(getApplicationContext(), "SMS sent", Toast.LENGTH_LONG).show();
            } catch(android.content.ActivityNotFoundException ex) {
                Toast.makeText(getApplicationContext(), "SMS failed", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == MY_SMS_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String enc = AESHelper.encrypt(message, "testseed");
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, enc, null, null);
                Toast.makeText(getApplicationContext(), "SMS sent", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "SMS failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}
