package com.berniac.vocalwarmup.ui.player;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Mikhail Lipkovich on 1/23/2018.
 */
public abstract class PlayerView extends AppCompatActivity {

    protected PlayerPresenter presenter;

    protected PlayerScreenFragment screenFragment;
    protected PlayerConfigFragment configFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PlayerPresenter();
        presenter.onAttach(this);

        screenFragment = new PlayerScreenFragment();
        screenFragment.setPresenter(presenter);

        configFragment = new PlayerConfigFragment();
        configFragment.setPresenter(presenter);
    }

    public abstract void changePlayButtonToPause();

    public abstract void changePlayButtonToPlay();

    public abstract void switchToConfigPanel();

    public abstract void switchToScreenPanel();
}
