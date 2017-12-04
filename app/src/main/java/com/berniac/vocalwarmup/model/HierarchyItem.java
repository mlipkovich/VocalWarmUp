package com.berniac.vocalwarmup.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by Mikhail Lipkovich on 11/30/2017.
 * Represents single item from the whole draws hierarchy. Can be either category or particular draw
 */
public class HierarchyItem {

    @SerializedName("type")
    protected ItemType type;

    protected HierarchyItem[] child;

    protected String name;

    protected String image;

    public enum ItemType {
        @SerializedName("category")
        CATEGORY,
        @SerializedName("draw")
        DRAW;
    }

    public ItemType getType() {
        return type;
    }

    public HierarchyItem[] getChild() {
        return child;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HierarchyItem that = (HierarchyItem) o;

        if (!Arrays.equals(child, that.child)) return false;
        if (type != that.type) return false;
        if (image != null ? !image.equals(that.image) : that.image!= null) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(child);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HierarchyItem{" +
                "child=" + Arrays.toString(child) +
                ", type=" + type +
                ", image=" + image +
                ", name='" + name + '\'' +
                '}';
    }
}
