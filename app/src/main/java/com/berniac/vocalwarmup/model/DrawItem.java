package com.berniac.vocalwarmup.model;

import java.util.Arrays;

/**
 * Created by Mikhail Lipkovich on 11/30/2017.
 */
public class DrawItem extends HierarchyItem {

    // TODO: what are the fields required for draws? At least harmony should be added
    protected String melody;
    protected String accompaniment;
    protected String sample;

    public DrawItem(HierarchyItem[] childItems, ItemType type, String name, String image,
                    String melody, String accompaniment, String sample) {
        this.child = childItems;
        this.type = type;
        this.name = name;
        this.image = image;
        this.melody = melody;
        this.accompaniment = accompaniment;
        this.sample = sample;
    }

    public String getMelody() {
        return melody;
    }

    public String getSample() {
        return sample;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DrawItem drawItem = (DrawItem) o;

        if (melody != null ? !melody.equals(drawItem.melody) : drawItem.melody != null) return false;
        return sample != null ? sample.equals(drawItem.sample) : drawItem.sample == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (melody != null ? melody.hashCode() : 0);
        result = 31 * result + (sample != null ? sample.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DrawItem{" +
                "child=" + Arrays.toString(child) +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", melody='" + melody + '\'' +
                ", sample='" + sample + '\'' +
                '}';
    }

    public String getAccompaniment() {
        return accompaniment;
    }
}
