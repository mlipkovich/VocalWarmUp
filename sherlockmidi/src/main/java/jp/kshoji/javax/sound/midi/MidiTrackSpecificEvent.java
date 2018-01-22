package jp.kshoji.javax.sound.midi;

import android.support.annotation.NonNull;

/**
 * Created by Mikhail Lipkovich on 12/17/2017.
 */
public class MidiTrackSpecificEvent extends MidiEvent {

    private int trackIndex;

    public MidiTrackSpecificEvent(@NonNull MidiEvent midiEvent, int trackIndex) {
        super(midiEvent.getMessage(), midiEvent.getTick());
        this.trackIndex = trackIndex;
    }

    public int getTrackIndex() {
        return trackIndex;
    }

    public void setTrackIndex(int trackIndex) {
        this.trackIndex = trackIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MidiTrackSpecificEvent that = (MidiTrackSpecificEvent) o;

        return trackIndex == that.trackIndex;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + trackIndex;
        return result;
    }

    @Override
    public String toString() {
        return "MidiTrackSpecificEvent{" +
                "trackIndex=" + trackIndex +
                "} " + super.toString();
    }
}
