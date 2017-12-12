package com.berniac.vocalwarmup.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.SeekBar;
import android.widget.TextView;

import com.berniac.vocalwarmup.R;


/**
 * Created by Mikhail Lipkovich on 12/10/2017.
 */
public class PlayerActivity extends AppCompatActivity {

    // TODO: Create appropriate presenter

    // TODO: Remove this ...
    private static final int SEEK_BAR_ZERO = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        ActionBar topBar = getSupportActionBar();
        // TODO: Sonya: It seems for me that it's better to do all these things in XML using custom styles
        // but I wasn't able to achieve it.
        topBar.setTitle("Тренировка");
        topBar.setDisplayHomeAsUpEnabled(true);
        topBar.setHomeAsUpIndicator(R.drawable.ic_cancel_black_24px);

        final TextView textView = (TextView) findViewById(R.id.current_tempo_text);

        SeekBar seekBar = (SeekBar) findViewById(R.id.tempo_seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String sign = progress - SEEK_BAR_ZERO > 0 ? "+" : "-";
                String text = sign + "0." + String.valueOf(Math.abs(progress - SEEK_BAR_ZERO)) + "x";
                textView.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO: Sonya: Isn't it overkill to use Menu here? Wasn't able to put regular button there
        getMenuInflater().inflate(R.menu.player_top_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
