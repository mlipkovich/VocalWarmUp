package com.berniac.vocalwarmup.sequence.sequencer;

import com.berniac.vocalwarmup.sequence.Instrument;

/**
 * Created by Mikhail Lipkovich on 8/26/2018.
 */
public class MidiEventShort {

    private final int eventType;
    private final Instrument instrument;
    private final int note;
    private final long position;

    public MidiEventShort(int eventType, Instrument instrument, int note, long position) {
        this.eventType = eventType;
        this.instrument = instrument;
        this.note = note;
        this.position = position;
    }

    public int getEventType() {
        return eventType;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public int getNote() {
        return note;
    }

    public long getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MidiEventShort that = (MidiEventShort) o;

        if (eventType != that.eventType) return false;
        if (note != that.note) return false;
        if (position != that.position) return false;
        return instrument == that.instrument;
    }

    @Override
    public int hashCode() {
        int result = eventType;
        result = 31 * result + instrument.hashCode();
        result = 31 * result + note;
        result = 31 * result + (int) (position ^ (position >>> 32));
        return result;
    }
}
