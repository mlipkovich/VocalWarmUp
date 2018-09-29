package com.berniac.vocalwarmup.ui.player;

import android.app.FragmentTransaction;
import android.os.Bundle;
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

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.player_fragment_container, configFragment);
        transaction.add(R.id.player_fragment_container, screenFragment);
        transaction.hide(configFragment);
        transaction.show(screenFragment);
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
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else {
            onSupportNavigateUp();
        }
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
    public void changeDirectionButtonToRight() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                revertButton.setImageResource(R.drawable.ic_player_straight);
            }
        });
    }

    @Override
    public void changeDirectionButtonToLeft() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                revertButton.setImageResource(R.drawable.ic_player_backwards);
            }
        });
    }

    @Override
    public void switchToConfigPanel() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        addAnimation(transaction);
        transaction.hide(screenFragment);
        transaction.show(configFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void switchToScreenPanel() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        addAnimation(transaction);
        transaction.hide(configFragment);
        transaction.show(screenFragment);
        getFragmentManager().popBackStack();
        transaction.commit();
    }

    private void addAnimation(FragmentTransaction transaction) {
        View fragmentPlayerScreen = screenFragment.getView();
        View fragmentPlayerConfig = configFragment.getView();
        float scale = this.getResources().getDisplayMetrics().density * 8000;
        fragmentPlayerScreen.setCameraDistance(scale);
        fragmentPlayerConfig.setCameraDistance(scale);

        transaction.setCustomAnimations(
                R.animator.card_flip_right_in,
                R.animator.card_flip_right_out,
                R.animator.card_flip_left_in,
                R.animator.card_flip_left_out);
    }
}
