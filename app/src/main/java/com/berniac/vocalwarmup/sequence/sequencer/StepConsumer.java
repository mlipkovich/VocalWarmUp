package com.berniac.vocalwarmup.sequence.sequencer;

/**
 * Created by Mikhail Lipkovich on 6/01/2018.
 */
public interface StepConsumer {

    WarmUpStep getNextStep() throws InterruptedException;

    WarmUpStep getPreviousStep() throws InterruptedException;
}
