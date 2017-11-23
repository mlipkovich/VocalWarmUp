package com.berniac.vocalwarmup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

// WIP
public class WarmUpConfigurationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warm_up_configuration);

        String[] items = new String[]{"C", "C#", "D", "D#"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);

        Spinner startNote = (Spinner)findViewById(R.id.spinner_start_note);
        Spinner lowerNote = (Spinner)findViewById(R.id.spinner_lower_note);
        Spinner upperNote = (Spinner)findViewById(R.id.spinner_upper_note);

        startNote.setAdapter(adapter);
        lowerNote.setAdapter(adapter);
        upperNote.setAdapter(adapter);
    }


}
