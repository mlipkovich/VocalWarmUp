package com.berniac.vocalwarmup.ui.player;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.berniac.vocalwarmup.R;


/**
 * Created by Mikhail Lipkovich on 12/10/2017.
 */
public class PlayerActivity extends PlayerView {

    // TODO: Remove this ...
    private static final int SEEK_BAR_ZERO = 25;

    private TextView tempoTextView;
    private ImageButton playButton;
    private ImageButton nextStepButton;
    private ImageButton previousStepButton;
    private ImageButton repeatButton;
    private ImageButton revertButton;

    // TODO: Will be moved to separate panel
    private ImageButton harmonySwitcherButton;
    private ImageButton melodySwitcherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        ActionBar topBar = getSupportActionBar();
        topBar.setTitle("Тренировка");
        topBar.setDisplayHomeAsUpEnabled(true);
        topBar.setHomeAsUpIndicator(R.drawable.ic_player_close);
        topBar.setElevation(0);

        tempoTextView = (TextView) findViewById(R.id.current_tempo_text);

        SeekBar seekBar = (SeekBar) findViewById(R.id.tempo_seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                presenter.onTempoChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        playButton = (ImageButton) findViewById(R.id.play_btn);
        playButton.setBackgroundResource(R.drawable.ic_player_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onPlayClicked();
            }
        });

        nextStepButton = (ImageButton) findViewById(R.id.next_step_btn);
        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onNextClicked();
            }
        });

        previousStepButton = (ImageButton) findViewById(R.id.previous_step_btn);
        previousStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onPreviousClicked();
            }
        });

        repeatButton = (ImageButton) findViewById(R.id.repeat_btn);
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onRepeatClicked();
            }
        });

        harmonySwitcherButton = (ImageButton) findViewById(R.id.harmony_btn);
        harmonySwitcherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onHarmonySwitcherClicked();
            }
        });

        melodySwitcherButton = (ImageButton) findViewById(R.id.melody_btn);
        melodySwitcherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onMelodySwitcherClicked();
            }
        });
    }

    @Override
    public void changeTempoProgress(int progress) {
        String sign = progress - SEEK_BAR_ZERO > 0 ? "+" : "-";
        String text = sign + "0." + String.valueOf(Math.abs(progress - SEEK_BAR_ZERO)) + "x";
        tempoTextView.setText(text);
    }

    @Override
    public void changePlayButtonToPause() {
        playButton.setBackgroundResource(R.drawable.ic_player_pause);
    }

    @Override
    public void changePlayButtonToPlay() {
        playButton.setBackgroundResource(R.drawable.ic_player_play);
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
