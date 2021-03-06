package com.berniac.vocalwarmup.ui.player;

import android.app.Fragment;
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
    protected ImageButton metronomeSwitcherButton;

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
        melodySwitcherButton = (VibratingImageButton) view.findViewById(buttonId);
        melodySwitcherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onMelodySwitcherClicked();
            }
        });
    }

    public void initHarmonySwitcher(View view, int buttonId) {
        harmonySwitcherButton = (VibratingImageButton) view.findViewById(buttonId);
        harmonySwitcherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onHarmonySwitcherClicked();
            }
        });
    }

    public void initAdjustmentSwitcher(View view, int buttonId) {
        adjustmentSwitcherButton = (VibratingImageButton) view.findViewById(buttonId);
        adjustmentSwitcherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onAdjustmentSwitcherClicked();
            }
        });
    }

    public void initMetronomeSwitcher(View view, int buttonId) {
        metronomeSwitcherButton = (VibratingImageButton) view.findViewById(buttonId);
        metronomeSwitcherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onMetronomeSwitcherClicked();
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

    public void changeMetronomeButtonToOn() {
        metronomeSwitcherButton.setImageResource(R.drawable.ic_player_metronome_on);
    }

    public void changeMetronomeButtonToOff() {
        metronomeSwitcherButton.setImageResource(R.drawable.ic_player_metronome_off);
    }
}
