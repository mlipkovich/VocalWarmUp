package com.berniac.vocalwarmup.sequence.sequencer;

import com.berniac.vocalwarmup.sequence.Direction;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import jp.kshoji.javax.sound.midi.MidiEvent;

/**
 * Created by Mikhail Lipkovich on 6/01/2018.
 */
public class WarmUpStep {

    public static final WarmUpStep EMPTY_STEP =
            new WarmUpStep(-1, -1, -1, null);

    private Direction direction;
    private int tonic;
    private int forwardTonic;
    private int backwardTonic;
    private Set<MidiEvent> baseEvents;
    private Set<MidiEvent> adjustmentForwardEvents;
    private Set<MidiEvent> adjustmentBackwardEvents;
    private Set<MidiEvent> adjustmentRepeatEvents;

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

    public Set<MidiEvent> getBaseEvents() {
        return baseEvents;
    }

    public Set<MidiEvent> getAdjustmentForwardEvents() {
        return adjustmentForwardEvents;
    }

    public Set<MidiEvent> getAdjustmentBackwardEvents() {
        return adjustmentBackwardEvents;
    }

    public Set<MidiEvent> getAdjustmentRepeatEvents() {
        return adjustmentRepeatEvents;
    }

    void setTonic(int tonic) {
        this.tonic = tonic;
    }

    void addBaseEvent(MidiEvent event) {
        baseEvents.add(event);
    }

    void addAdjustmentForwardEvent(MidiEvent event) {
        adjustmentForwardEvents.add(event);
    }

    void addAdjustmentBackwardEvent(MidiEvent event) {
        adjustmentBackwardEvents.add(event);
    }

    void addAdjustmentRepeatEvent(MidiEvent event) {
        adjustmentRepeatEvents.add(event);
    }

    private static class EventsComparator implements Comparator<MidiEvent> {
        @Override
        public int compare(MidiEvent event1, MidiEvent event2) {
            if (event1.equals(event2)) {
                return 0;
            }
            return (event1.getTick() < event2.getTick()) ? -1 : 1;
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
