package com.berniac.vocalwarmup.ui.model;

import com.berniac.vocalwarmup.model.HierarchyItem;
import com.berniac.vocalwarmup.model.Preset;

import java.util.List;

/**
 * Created by Mikhail Lipkovich on 11/28/2017.
 */
public interface IWarmUpRepository {

    HierarchyItem[] getItemsByHierarchy(List<Integer> categoryIds);

    Preset[] getPresetItems();

    // TODO: Probably use just index instead
    void setSelectedItem(HierarchyItem item);

    HierarchyItem getSelectedItem();
}
