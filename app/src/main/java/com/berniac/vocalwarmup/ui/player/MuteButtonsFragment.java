package com.berniac.vocalwarmup.ui.player;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.berniac.vocalwarmup.R;

/**
 * Created by Mikhail Lipkovich on 8/2/2018.
 */
public class MuteButtonsFragment extends Fragment {
    protected PlayerPresenter presenter;

    protected ImageButton harmonySwitcherButton;
    protected ImageButton melodySwitcherButton;
    protected ImageButton adjustmentSwitcherButton;


    protected static abstract class ProgressSeekBarListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }

    public void setPresenter(PlayerPresenter presenter) {
        this.presenter = presenter;
    }

    public void initMelodySwitcher(View view, int buttonId) {
        melodySwitcherButton = (ImageButton) view.findViewById(buttonId);
        melodySwitcherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onMelodySwitcherClicked();
            }
        });
    }

    public void initHarmonySwitcher(View view, int buttonId) {
        harmonySwitcherButton = (ImageButton) view.findViewById(buttonId);
        harmonySwitcherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onHarmonySwitcherClicked();
            }
        });
    }

    public void initAdjustmentSwitcher(View view, int buttonId) {
        adjustmentSwitcherButton = (ImageButton) view.findViewById(buttonId);
        adjustmentSwitcherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onAdjustmentSwitcherClicked();
            }
        });
    }

    public void changeHarmonyButtonToOn() {
        harmonySwitcherButton.setImageResource(R.drawable.ic_player_harmony_on);
    }

    public void changeHarmonyButtonToOff() {
        harmonySwitcherButton.setImageResource(R.drawable.ic_player_harmony_off);
    }

    public void changeMelodyButtonToOn() {
        melodySwitcherButton.setImageResource(R.drawable.ic_player_melody_on);
    }

    public void changeMelodyButtonToOff() {
        melodySwitcherButton.setImageResource(R.drawable.ic_player_melody_off);
    }

    public void changeAdjustmentButtonToOn() {
        adjustmentSwitcherButton.setImageResource(R.drawable.ic_player_key_on);
    }

    public void changeAdjustmentButtonToOff() {
        adjustmentSwitcherButton.setImageResource(R.drawable.ic_player_key_off);
    }
}
