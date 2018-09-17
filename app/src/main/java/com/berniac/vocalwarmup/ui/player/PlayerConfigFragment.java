package com.berniac.vocalwarmup.ui.player;

import android.os.Bundle;
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
public class PlayerConfigFragment extends MuteButtonsFragment {

    private static final int ZERO_TEMPO = 40;

    private ImageButton screenPanelButton;
    private TextView globalTempoTextView;
    private SeekBar globalTempoSeekBar;

    private SeekBar melodyVolumeSeekBar;
    private SeekBar harmonyVolumeSeekBar;
    private SeekBar adjustmentVolumeSeekBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_config, container, false);

        globalTempoTextView = (TextView) view.findViewById(R.id.current_tempo_text);

        globalTempoSeekBar = (SeekBar) view.findViewById(R.id.tempo_seek_bar);
        globalTempoSeekBar.setOnSeekBarChangeListener(new ProgressSeekBarListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                presenter.onGlobalTempoChanged(progress);
            }
        });

        melodyVolumeSeekBar = (SeekBar) view.findViewById(R.id.melody_seek_bar);
        melodyVolumeSeekBar.setOnSeekBarChangeListener(new ProgressSeekBarListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                presenter.onMelodyVolumeChanged(progress);
            }
        });

        harmonyVolumeSeekBar = (SeekBar) view.findViewById(R.id.harmony_seek_bar);
        harmonyVolumeSeekBar.setOnSeekBarChangeListener(new ProgressSeekBarListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                presenter.onHarmonyVolumeChanged(progress);
            }
        });

        adjustmentVolumeSeekBar = (SeekBar) view.findViewById(R.id.adjustment_seek_bar);
        adjustmentVolumeSeekBar.setOnSeekBarChangeListener(new ProgressSeekBarListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                presenter.onAdjustmentVolumeChanged(progress);
            }
        });

        screenPanelButton = (VibratingImageButton) view.findViewById(R.id.back_player_image);
        screenPanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onScreenPanelClicked();
            }
        });

        initMelodySwitcher(view, R.id.melody_btn);
        initHarmonySwitcher(view, R.id.harmony_btn);
        initAdjustmentSwitcher(view, R.id.adjustment_btn);
        initMetronomeSwitcher(view, R.id.tempo_btn);

        return view;
    }

    @Override
    public void setPresenter(PlayerPresenter presenter) {
        super.setPresenter(presenter);
        presenter.onAttachConfigFragment(this);
    }

    public int changeMelodyVolumeBar(int progress) {
        int previousProgress = melodyVolumeSeekBar.getProgress();
        melodyVolumeSeekBar.setProgress(progress);
        return previousProgress;
    }

    public int changeHarmonyVolumeBar(int progress) {
        int previousProgress = harmonyVolumeSeekBar.getProgress();
        harmonyVolumeSeekBar.setProgress(progress);
        return previousProgress;
    }

    public int changeAdjustmentVolumeBar(int progress) {
        int previousProgress = adjustmentVolumeSeekBar.getProgress();
        adjustmentVolumeSeekBar.setProgress(progress);
        return previousProgress;
    }

    public int changeGlobalTempoProgress(int progress) {
        int progressValue = ZERO_TEMPO + progress;
        globalTempoTextView.setText(String.valueOf(progressValue));
        return progressValue;
    }
}