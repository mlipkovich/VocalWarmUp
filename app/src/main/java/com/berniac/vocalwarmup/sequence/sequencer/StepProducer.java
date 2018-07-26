package com.berniac.vocalwarmup.sequence.sequencer;

import com.berniac.vocalwarmup.sequence.Direction;
import com.berniac.vocalwarmup.sequence.WarmUp;

/**
 * Created by Mikhail Lipkovich on 6/1/2018.
 */
public interface StepProducer {

    void restart();

    void generateSteps() throws InterruptedException;

    void cleanGenerated();

    void changeDirection(int startingTonic, Direction directionToChange);
}
