package com.berniac.vocalwarmup.ui.player;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.berniac.vocalwarmup.R;

/**
 * Created by Mikhail Lipkovich on 2/16/2018.
 */
public class PlayerScreenFragment extends Fragment {

    // TODO: Remove this ...
    private static final int SEEK_BAR_ZERO = 25;

    private PlayerPresenter presenter;

    private TextView tempoTextView;
    private SeekBar seekBar;

    // TODO: Will be removed from here
    private ImageButton harmonySwitcherButton;
    private ImageButton melodySwitcherButton;
    private ImageButton configPanelButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_player_screen, container, false);

        tempoTextView = (TextView) view.findViewById(R.id.current_tempo_text);

        seekBar = (SeekBar) view.findViewById(R.id.tempo_seek_bar);
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

        harmonySwitcherButton = (ImageButton) view.findViewById(R.id.harmony_btn);
        harmonySwitcherButton.setBackgroundResource(R.drawable.ic_player_harmony_on);
        harmonySwitcherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onHarmonySwitcherClicked();
            }
        });

        melodySwitcherButton = (ImageButton) view.findViewById(R.id.melody_btn);
        melodySwitcherButton.setBackgroundResource(R.drawable.ic_player_melody_on);
        melodySwitcherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onMelodySwitcherClicked();
            }
        });


        configPanelButton = (ImageButton) view.findViewById(R.id.configure_player_image);
        configPanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onConfigPanelClicked();
            }
        });

        return view;
    }

    public void setPresenter(final PlayerPresenter presenter) {
        this.presenter = presenter;
        this.presenter.onAttachScreenFragment(this);
    }

    public void changeTempoProgress(int progress) {
        String sign = progress - SEEK_BAR_ZERO > 0 ? "+" : "-";
        String text = sign + "0." + String.valueOf(Math.abs(progress - SEEK_BAR_ZERO)) + "x";
        tempoTextView.setText(text);
    }

    public void changeHarmonyButtonToOn() {
        harmonySwitcherButton.setBackgroundResource(R.drawable.ic_player_harmony_on);
    }

    public void changeHarmonyButtonToOff() {
        harmonySwitcherButton.setBackgroundResource(R.drawable.ic_player_harmony_off);
    }

    public void changeMelodyButtonToOn() {
        melodySwitcherButton.setBackgroundResource(R.drawable.ic_player_melody_on);
    }

    public void changeMelodyButtonToOff() {
        melodySwitcherButton.setBackgroundResource(R.drawable.ic_player_melody_off);
    }
}