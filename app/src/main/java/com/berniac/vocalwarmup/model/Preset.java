package com.berniac.vocalwarmup.model;

import com.berniac.vocalwarmup.sequence.Direction;

import java.util.Arrays;

/**
 * Created by Mikhail Lipkovich on 6/9/2018.
 */
public class Preset extends DrawItem {

    protected Direction[] directions;
    protected int pauseSize;
    protected int step;
    protected String lowerNote;
    protected String startingNote;
    protected String upperNote;

    public Preset(HierarchyItem[] childItems, ItemType type, String name, String image, String melody, String accompaniment, String sample) {
        super(childItems, type, name, image, melody, accompaniment, sample);
    }

    @Override
    public String toString() {
        return "Preset{" +
                "directions=" + Arrays.toString(directions) +
                ", pauseSize=" + pauseSize +
                ", stepSize=" + step +
                ", lowerNote='" + lowerNote + '\'' +
                ", startingNote='" + startingNote + '\'' +
                ", upperNote='" + upperNote + '\'' +
                ", melody='" + melody + '\'' +
                ", accompaniment='" + accompaniment + '\'' +
                ", sample='" + sample + '\'' +
                ", type=" + type +
                ", child=" + Arrays.toString(child) +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public Direction[] getDirections() {
        return directions;
    }

    public int getPauseSize() {
        return pauseSize;
    }

    public int getStep() {
        return step;
    }

    public String getLowerNote() {
        return lowerNote;
    }

    public String getStartingNote() {
        return startingNote;
    }

    public String getUpperNote() {
        return upperNote;
    }
}
