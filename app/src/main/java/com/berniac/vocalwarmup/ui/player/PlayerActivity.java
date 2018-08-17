package com.berniac.vocalwarmup.ui.player;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

import com.berniac.vocalwarmup.R;


/**
 * Created by Mikhail Lipkovich on 12/10/2017.
 */
public class PlayerActivity extends PlayerView {

    private ImageButton playButton;
    private ImageButton nextStepButton;
    private ImageButton previousStepButton;
    private ImageButton repeatButton;
    private ImageButton revertButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        ActionBar topBar = getSupportActionBar();
        topBar.setTitle("Тренировка");
        topBar.setDisplayHomeAsUpEnabled(true);
        topBar.setHomeAsUpIndicator(R.drawable.ic_player_close);
        topBar.setElevation(0);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.player_fragment_container, configFragment);
        transaction.add(R.id.player_fragment_container, screenFragment);
        transaction.commit();

        playButton = (VibratingImageButton) findViewById(R.id.play_btn);
        playButton.setImageResource(R.drawable.ic_player_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onPlayClicked();
            }
        });

        nextStepButton = (VibratingImageButton) findViewById(R.id.next_step_btn);
        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onNextClicked();
            }
        });

        previousStepButton = (VibratingImageButton) findViewById(R.id.previous_step_btn);
        previousStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onPreviousClicked();
            }
        });

        repeatButton = (VibratingImageButton) findViewById(R.id.repeat_btn);
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onRepeatClicked();
            }
        });

        revertButton = (VibratingImageButton) findViewById(R.id.change_direction_btn);
        revertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onRevertClicked();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        presenter.onNavigateUp();
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        // TODO: Perhaps different behaviour
        onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.player_top_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void changePlayButtonToPause() {
        playButton.setImageResource(R.drawable.ic_player_pause);
    }

    @Override
    public void changePlayButtonToPlay() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playButton.setImageResource(R.drawable.ic_player_play);
            }
        });
    }

    @Override
    public void switchToConfigPanel() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(screenFragment);
        transaction.show(configFragment);
        transaction.commit();
    }

    @Override
    public void switchToScreenPanel() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(configFragment);
        transaction.show(screenFragment);
        transaction.commit();
    }

    @Override
    public void changeDirection() {
        // TODO: Change image with direction
    }
}
