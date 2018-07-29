package com.berniac.vocalwarmup.music;

/**
 * Created by Mikhail Lipkovich on 1/19/2018.
 */
public class FixedStep implements Step {

    private int numberOfSemitones;

    public FixedStep(int numberOfSemitones) {
        this.numberOfSemitones = numberOfSemitones;
    }

    @Override
    public int getNextShift() {
        return numberOfSemitones;
    }
}
