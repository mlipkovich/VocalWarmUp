package com.berniac.vocalwarmup.ui.training.presets;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.berniac.vocalwarmup.R;
import com.berniac.vocalwarmup.ui.ResourcesProvider;
import com.berniac.vocalwarmup.ui.model.WarmUpRepository;

import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by Mikhail Lipkovich on 11/28/2017.
 */
public class PresetsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_presets, container, false);
        RecyclerView presetsList = (RecyclerView) view.findViewById(R.id.list_preset_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        presetsList.setLayoutManager(layoutManager);
        presetsList.setItemAnimator(new DefaultItemAnimator());
        presetsList.setHasFixedSize(true);

        PresetsListPresenter presetsListPresenter = new PresetsListPresenter();
        RecyclerView.Adapter adapter = new PresetsListAdapter(presetsListPresenter, getContext());
        presetsList.setAdapter(adapter);

        return view;
    }
}
