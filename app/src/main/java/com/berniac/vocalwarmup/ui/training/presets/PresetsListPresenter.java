package com.berniac.vocalwarmup.ui.training.presets;

import com.berniac.vocalwarmup.model.Preset;
import com.berniac.vocalwarmup.ui.model.IWarmUpRepository;
import com.berniac.vocalwarmup.ui.model.RepositoryFactory;
import com.berniac.vocalwarmup.ui.training.ItemRowView;

/**
 * Created by Mikhail Lipkovich on 11/28/2017.
 */
public class PresetsListPresenter {

    private IWarmUpRepository repository;
    private Preset[] draws;
    private PresetsListAdapter view;

    public PresetsListPresenter() {
        this.repository = RepositoryFactory.getRepository();
        this.draws = repository.getPresetItems();
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
        repository.setSelectedItem(draws[clickedItemPosition]);
        view.switchToPlayer();
    }
}
