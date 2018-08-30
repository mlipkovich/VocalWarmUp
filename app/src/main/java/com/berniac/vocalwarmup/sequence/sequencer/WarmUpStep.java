package com.berniac.vocalwarmup.sequence.sequencer;

import com.berniac.vocalwarmup.sequence.Direction;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Mikhail Lipkovich on 6/01/2018.
 */
public class WarmUpStep {

    public static final WarmUpStep FINAL_STEP =
            new WarmUpStep(-1, -1, -1, null);

    private Direction direction;
    private int tonic;
    private int forwardTonic;
    private int backwardTonic;
    private Set<MidiEventShort> baseEvents;
    private Set<MidiEventShort> adjustmentForwardEvents;
    private Set<MidiEventShort> adjustmentBackwardEvents;
    private Set<MidiEventShort> adjustmentRepeatEvents;

    WarmUpStep(int tonic, int forwardTonic, int backwardTonic, Direction direction) {
        this.tonic = tonic;
        this.forwardTonic = forwardTonic;
        this.backwardTonic = backwardTonic;
        this.direction = direction;
        this.baseEvents = new TreeSet<>(new EventsComparator());
        this.adjustmentForwardEvents = new TreeSet<>(new EventsComparator());
        this.adjustmentBackwardEvents = new TreeSet<>(new EventsComparator());
        this.adjustmentRepeatEvents = new TreeSet<>(new EventsComparator());
    }

    public Direction getDirection() {
        return direction;
    }

    public int getTonic() {
        return tonic;
    }

    public int getForwardTonic() {
        return forwardTonic;
    }

    public int getBackwardTonic() {
        return backwardTonic;
    }

    public Set<MidiEventShort> getBaseEvents() {
        return baseEvents;
    }

    public Set<MidiEventShort> getAdjustmentForwardEvents() {
        return adjustmentForwardEvents;
    }

    public Set<MidiEventShort> getAdjustmentBackwardEvents() {
        return adjustmentBackwardEvents;
    }

    public Set<MidiEventShort> getAdjustmentRepeatEvents() {
        return adjustmentRepeatEvents;
    }

    void setTonic(int tonic) {
        this.tonic = tonic;
    }

    void addBaseEvent(MidiEventShort event) {
        baseEvents.add(event);
    }

    void addAdjustmentForwardEvent(MidiEventShort event) {
        adjustmentForwardEvents.add(event);
    }

    void addAdjustmentBackwardEvent(MidiEventShort event) {
        adjustmentBackwardEvents.add(event);
    }

    void addAdjustmentRepeatEvent(MidiEventShort event) {
        adjustmentRepeatEvents.add(event);
    }

    private static class EventsComparator implements Comparator<MidiEventShort> {
        @Override
        public int compare(MidiEventShort event1, MidiEventShort event2) {
            if (event1.equals(event2)) {
                return 0;
            }
            return (event1.getPosition() < event2.getPosition()) ? -1 : 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WarmUpStep that = (WarmUpStep) o;

        if (tonic != that.tonic) return false;
        if (forwardTonic != that.forwardTonic) return false;
        if (backwardTonic != that.backwardTonic) return false;
        if (direction != that.direction) return false;
        if (!baseEvents.equals(that.baseEvents)) return false;
        if (!adjustmentForwardEvents.equals(that.adjustmentForwardEvents)) return false;
        if (!adjustmentBackwardEvents.equals(that.adjustmentBackwardEvents)) return false;
        return adjustmentRepeatEvents.equals(that.adjustmentRepeatEvents);
    }

    @Override
    public int hashCode() {
        int result = direction != null ? direction.hashCode() : 0;
        result = 31 * result + tonic;
        result = 31 * result + forwardTonic;
        result = 31 * result + backwardTonic;
        result = 31 * result + baseEvents.hashCode();
        result = 31 * result + adjustmentForwardEvents.hashCode();
        result = 31 * result + adjustmentBackwardEvents.hashCode();
        result = 31 * result + adjustmentRepeatEvents.hashCode();
        return result;
    }
}
