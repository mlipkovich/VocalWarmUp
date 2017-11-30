package com.berniac.vocalwarmup.ui.training;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.berniac.vocalwarmup.R;
import com.berniac.vocalwarmup.ui.training.patterns.PatternAdapter;

/**
 * Created by Mikhail Lipkovich on 11/28/2017.
 */
// TODO: Ugly. Think about better architecture
public class QuickStartFragment extends Fragment {

    private QuickStartPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quick_start, container, false);
        RecyclerView warmUpList = (RecyclerView) view.findViewById(R.id.listPresets);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        warmUpList.setLayoutManager(layoutManager);
        warmUpList.setItemAnimator(new DefaultItemAnimator());
        warmUpList.setHasFixedSize(true);
        warmUpList.setAdapter(new PatternAdapter(presenter.getPatternListPresenter()));

        presenter.onAttach(this);
        return view;
    }

    public void setPresenter(QuickStartPresenter presenter) {
        this.presenter = presenter;
    }
}
