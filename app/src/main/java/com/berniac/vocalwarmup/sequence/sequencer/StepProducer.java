package com.berniac.vocalwarmup.sequence.sequencer;

import com.berniac.vocalwarmup.sequence.Direction;

/**
 * Created by Mikhail Lipkovich on 6/1/2018.
 */
public interface StepProducer {
    void generateSteps() throws InterruptedException;

    void cleanGenerated();

    void changeDirection(int startingTonic, Direction directionToChange);
}
