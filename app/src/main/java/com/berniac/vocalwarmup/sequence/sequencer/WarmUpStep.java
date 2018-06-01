package com.berniac.vocalwarmup.sequence.sequencer;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import jp.kshoji.javax.sound.midi.MidiEvent;

/**
 * Created by Mikhail Lipkovich on 6/01/2018.
 */
public class WarmUpStep {
    Set<MidiEvent> events;

    WarmUpStep() {
        this.events = new TreeSet<>(new Comparator<MidiEvent>() {
            @Override
            public int compare(MidiEvent event1, MidiEvent event2) {
                if (event1.equals(event2)) {
                    return 0;
                }
                return (event1.getTick() < event2.getTick()) ? -1 : 1;
            }
        });
    }

    void add(MidiEvent event) {
        events.add(event);
    }
}
