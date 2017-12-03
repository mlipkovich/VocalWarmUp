package com.berniac.vocalwarmup.model;

import java.util.Arrays;

/**
 * Created by Mikhail Lipkovich on 11/30/2017.
 */
public class DrawItem extends HierarchyItem {

    // TODO: what are the fields required for draws? At least harmony should be added
    protected String voice;
    protected String sample;

    public DrawItem(HierarchyItem[] childItems, ItemType type, String name, String image,
                    String voice, String sample) {
        this.child = childItems;
        this.type = type;
        this.name = name;
        this.image = image;
        this.voice = voice;
        this.sample = sample;
    }

    public String getVoice() {
        return voice;
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

        if (voice != null ? !voice.equals(drawItem.voice) : drawItem.voice != null) return false;
        return sample != null ? sample.equals(drawItem.sample) : drawItem.sample == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (voice != null ? voice.hashCode() : 0);
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
                ", voice='" + voice + '\'' +
                ", sample='" + sample + '\'' +
                '}';
    }
}
