package com.berniac.vocalwarmup.ui.model;

import com.berniac.vocalwarmup.model.HierarchyItem;

import java.util.List;

/**
 * Created by Mikhail Lipkovich on 11/28/2017.
 */
public interface IWarmUpRepository {

    HierarchyItem[] getItemsByHierarchy(List<Integer> categoryIds);
}
