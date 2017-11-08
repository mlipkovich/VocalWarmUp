package com.berniac.vocalwarmup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.Arrays;

import cn.sherlock.com.sun.media.sound.SF2Soundbank;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            SF2Soundbank sf = new SF2Soundbank(getAssets().open("SmallTimGM6mb.sf2"));
            System.out.println(Arrays.toString(sf.getInstruments()));
        } catch (IOException e) {
            //
        }
    }
}
