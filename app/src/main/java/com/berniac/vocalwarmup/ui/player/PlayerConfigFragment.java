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
public class PlayerConfigFragment extends Fragment {

    private PlayerPresenter presenter;
    private ImageButton screenPanelButton;
    private TextView globalTempoTextView;
    private SeekBar globalTempoSeekBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_config, container, false);

        globalTempoTextView = (TextView) view.findViewById(R.id.current_tempo_text);

        globalTempoSeekBar = (SeekBar) view.findViewById(R.id.tempo_seek_bar);
        globalTempoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                presenter.onGlobalTempoChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        screenPanelButton = (ImageButton) view.findViewById(R.id.back_player_image);
        screenPanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onScreenPanelClicked();
            }
        });

        return view;
    }

    public void setPresenter(final PlayerPresenter presenter) {
        this.presenter = presenter;
        presenter.onAttachConfigFragment(this);
    }

    public int changeGlobalTempoProgress(int progress) {
        int progressValue = 40 + progress;
        globalTempoTextView.setText(String.valueOf(progressValue));
        return progressValue;
    }
}