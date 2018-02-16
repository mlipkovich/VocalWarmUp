package com.berniac.vocalwarmup.ui.player;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.berniac.vocalwarmup.R;

/**
 * Created by Mikhail Lipkovich on 2/16/2018.
 */
public class PlayerConfigFragment extends Fragment {

    private PlayerPresenter presenter;
    private ImageButton screenPanelButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_config, container, false);

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
}