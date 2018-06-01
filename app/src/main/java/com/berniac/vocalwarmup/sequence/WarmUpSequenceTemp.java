package com.berniac.vocalwarmup.sequence;

import jp.kshoji.javax.sound.midi.Sequence;

/**
 * Created by Mikhail Lipkovich on 1/23/2018.
 */
public class WarmUpSequenceTemp {
    private Sequence sequence;
    private int lowestTonic;
    private int highestTonic;

    public WarmUpSequenceTemp(Sequence sequence, int lowerTonic, int higherTonic) {
        this.sequence = sequence;
        this.lowestTonic = lowerTonic;
        this.highestTonic = higherTonic;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public int getLowestTonic() {
        return lowestTonic;
    }

    public int getHighestTonic() {
        return highestTonic;
    }
}
