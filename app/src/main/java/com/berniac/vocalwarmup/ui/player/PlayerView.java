package com.berniac.vocalwarmup.ui.player;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Mikhail Lipkovich on 1/23/2018.
 */
public abstract class PlayerView extends AppCompatActivity {

    protected PlayerPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PlayerPresenter();
        presenter.onAttach(this);
    }

    public abstract void changeTempoProgress(int progress);

    public abstract void changePlayButtonToPause();

    public abstract void changePlayButtonToPlay();
}
