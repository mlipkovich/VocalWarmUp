package com.berniac.vocalwarmup.ui.training.library;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mikhail Lipkovich on 12/6/2017.
 */
public final class SelectedCategories {

    private List<Integer> categoryIds = new LinkedList<>();
    public static final SelectedCategories INSTANCE = new SelectedCategories();

    private SelectedCategories(){}

    public void goDownByHierarchy(int categoryId) {
        categoryIds.add(categoryId);
    }

    public void goUpByHierarchy() {
        categoryIds.remove(categoryIds.size() - 1);
    }

    public boolean isTopLevel() {
        return categoryIds.size() == 0;
    }

    public List<Integer> getCategoryIds() {
        return new ArrayList<>(categoryIds);
    }
}
