package com.example.texttospeech;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button convert;
    private EditText textToBeConverted;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        convert = findViewById(R.id.convert_button);
        textToBeConverted = findViewById(R.id.convert_text);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                    Toast.makeText(MainActivity.this, "Init done", Toast.LENGTH_LONG).show();
                }
            }
        });

        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toSpeak = textToBeConverted.getText().toString();
                textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }
}
