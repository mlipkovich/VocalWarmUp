package com.berniac.vocalwarmup.ui.model;

import com.berniac.vocalwarmup.model.DrawHierarchyJsonParser;
import com.berniac.vocalwarmup.model.HierarchyItem;
import com.berniac.vocalwarmup.model.Preset;

import java.io.Reader;
import java.util.List;

/**
 * Created by Mikhail Lipkovich on 11/27/2017.
 */
public class WarmUpRepository implements IWarmUpRepository {

    private HierarchyItem[] hierarchyItems;
    private Preset[] presetItems;
    private HierarchyItem selectedItem;

    // TODO: Do not parse JSON each time
    public WarmUpRepository(Reader library, Reader presets) {
        hierarchyItems = DrawHierarchyJsonParser.parseItemArray(library);
        presetItems = DrawHierarchyJsonParser.parsePresetArray(presets);
    }

    @Override
    public HierarchyItem[] getItemsByHierarchy(List<Integer> categoryIds) {
        HierarchyItem[] currentLevel = hierarchyItems;
        for (int categoryId : categoryIds) {
            currentLevel = currentLevel[categoryId].getChild();
        }

        return currentLevel;
    }

    @Override
    public Preset[] getPresetItems() {
        return presetItems;
    }

    @Override
    public void setSelectedItem(HierarchyItem item) {
        this.selectedItem = item;
    }

    @Override
    public HierarchyItem getSelectedItem() {
        return selectedItem;
    }
}
