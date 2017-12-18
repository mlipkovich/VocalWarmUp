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
}
