package com.berniac.vocalwarmup.ui.model;

import com.berniac.vocalwarmup.model.DrawHierarchyJsonParser;
import com.berniac.vocalwarmup.model.HierarchyItem;

import java.io.Reader;
import java.util.List;

/**
 * Created by Mikhail Lipkovich on 11/27/2017.
 */
public class WarmUpRepository implements IWarmUpRepository {

    private HierarchyItem[] hierarchyItems;

    // TODO: Do not parse JSON each time
    public WarmUpRepository(Reader reader) {
        hierarchyItems = DrawHierarchyJsonParser.parseItemArray(reader);
    }

    @Override
    public HierarchyItem[] getItemsByHierarchy(List<Integer> categoryIds) {
        HierarchyItem[] currentLevel = hierarchyItems;
        for (int categoryId : categoryIds) {
            currentLevel = currentLevel[categoryId].getChild();
        }

        return currentLevel;
    }
}
