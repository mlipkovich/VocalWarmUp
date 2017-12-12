package com.berniac.vocalwarmup.ui.training.presets;

import com.berniac.vocalwarmup.model.HierarchyItem;
import com.berniac.vocalwarmup.ui.model.IWarmUpRepository;
import com.berniac.vocalwarmup.ui.training.ItemRowView;

import java.util.Arrays;

/**
 * Created by Mikhail Lipkovich on 11/28/2017.
 */
public class PresetsListPresenter {

    private IWarmUpRepository repository;
    private HierarchyItem[] draws;
    private PresetsListAdapter view;

    public PresetsListPresenter(IWarmUpRepository repository) {
        this.repository = repository;

        // TODO: Separate JSON and method for getting presets
        this.draws = repository.getItemsByHierarchy(Arrays.asList(0, 0));
    }

    public void onAttach(PresetsListAdapter view) {
        this.view = view;
    }

    public void onBindDrawAtPosition(ItemRowView rowView, int position) {
        rowView.setTitle(draws[position].getName());
        rowView.setImage(draws[position].getImage());
    }

    public int getDrawsCount() {
        return draws.length;
    }

    public void onItemClicked(int clickedItemPosition) {
        // TODO: Add methods parameters
        view.switchToPlayer();
    }
}
