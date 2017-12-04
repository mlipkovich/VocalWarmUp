package com.berniac.vocalwarmup.model;

import java.util.Arrays;

/**
 * Created by Mikhail Lipkovich on 11/30/2017.
 */
public class CategoryItem extends HierarchyItem {

    public CategoryItem(HierarchyItem[] child, String image, ItemType type, String name) {
        this.child = child;
        this.image = image;
        this.type = type;
        this.name = name;
    }

    @Override
    public String toString() {
        return "CategoryItem{" +
                "child=" + Arrays.toString(child) +
                ", type=" + type +
                ", name='" + name + '\'' +
                "}";
    }
}
