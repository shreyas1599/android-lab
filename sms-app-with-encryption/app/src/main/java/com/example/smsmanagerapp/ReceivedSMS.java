package com.example.smsmanagerapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ReceivedSMS extends AppCompatActivity {

    public List<String> getSMS(){
        List<String> sms = new ArrayList<String>();
        Uri uriSMSURI = Uri.parse("content://sms/sent");
        Cursor cur = getContentResolver().query(uriSMSURI, null, null, null, null);

        while (cur != null && cur.moveToNext()) {
            String address = cur.getString(cur.getColumnIndex("address"));
            String body = cur.getString(cur.getColumnIndexOrThrow("body"));
            sms.add("Number: " + address + "\nMessage: " + body);
        }

        if (cur != null) {
            cur.close();
        }
        return sms;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_s_m_s);
        Intent intent = getIntent();
        String value = intent.getStringExtra("key");

        final ListView list = findViewById(R.id.listView);
        List<String> arrayList = getSMS();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = (String) list.getItemAtPosition(position);
                String separator = "Message: ";
                int sepPos = clickedItem.indexOf(separator);
                String mes = clickedItem.substring(sepPos + separator.length());
                mes = AESHelper.decrypt(mes, "testseed");
                Toast.makeText(ReceivedSMS.this, "The decrypted SMS is: " + mes, Toast.LENGTH_LONG).show();
            }
        });
    }
}