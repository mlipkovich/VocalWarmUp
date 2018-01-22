package com.berniac.vocalwarmup.music;

/**
 * Created by Mikhail Lipkovich on 1/19/2018.
 */
public class FixedStep implements Step {

    private int numberOfSemitones;

    public FixedStep(int numberOfQuarters) {
        this.numberOfSemitones = numberOfQuarters;
    }

    @Override
    public int getNextShift() {
        return numberOfSemitones;
    }
}
