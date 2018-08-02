package com.berniac.vocalwarmup.ui.player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.berniac.vocalwarmup.R;

import java.util.Locale;

/**
 * Created by Mikhail Lipkovich on 2/16/2018.
 */
public class PlayerScreenFragment extends MuteButtonsFragment {

    private TextView drawTitle;
    private TextView tempoTextView;
    private SeekBar tempoFactorSeekBar;

    private ImageButton configPanelButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_player_screen, container, false);

        drawTitle = (TextView) view.findViewById(R.id.player_draw_title);
        presenter.onDrawTitleInitialized();

        tempoTextView = (TextView) view.findViewById(R.id.current_tempo_text);

        tempoFactorSeekBar = (SeekBar) view.findViewById(R.id.tempo_seek_bar);
        tempoFactorSeekBar.setOnSeekBarChangeListener(new ProgressSeekBarListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                presenter.onTempoFactorChanged(progress);
            }
        });

        configPanelButton = (ImageButton) view.findViewById(R.id.configure_player_image);
        configPanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onConfigPanelClicked();
            }
        });

        initMelodySwitcher(view, R.id.melody_btn);
        initHarmonySwitcher(view, R.id.harmony_btn);

        return view;
    }

    @Override
    public void setPresenter(final PlayerPresenter presenter) {
        super.setPresenter(presenter);
        this.presenter.onAttachScreenFragment(this);
    }

    public void setDrawTitle(String title) {
        drawTitle.setText(title);
    }

    public float changeTempoProgress(int progress) {
        float progressValue = (float)0.75 + (float)progress/100;
        tempoTextView.setText(String.format(Locale.ENGLISH, "%.2fx", progressValue));
        return progressValue;
    }
}