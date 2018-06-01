package com.berniac.vocalwarmup.sequence.sequencer;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Mikhail Lipkovich on 6/01/2018.
 */
public class QueueStepConsumer implements StepConsumer {

    private BlockingQueue<WarmUpStep> forwardSteps;
    private List<WarmUpStep> previousSteps;
    private int numberOfStepsBehind;
    private static final int MAX_NUMBER_OF_PREVIOUS_STEPS = 100;

    public QueueStepConsumer(BlockingQueue<WarmUpStep> forwardSteps) {
        this.forwardSteps = forwardSteps;
        this.previousSteps = new ArrayList<>();
        this.numberOfStepsBehind = 0;
    }

    @Override
    public WarmUpStep getNextStep() throws InterruptedException {
        numberOfStepsBehind = Math.max(numberOfStepsBehind - 1, 0);
        if (numberOfStepsBehind == 0) {
            WarmUpStep step = forwardSteps.take();
            previousSteps.add(step);    // TODO: Check max list size condition
            return step;
        }
        return previousSteps.get(previousSteps.size() - numberOfStepsBehind);
    }

    @Override
    public WarmUpStep getPreviousStep() {
        numberOfStepsBehind = Math.min(numberOfStepsBehind + 1, previousSteps.size());
        return previousSteps.get(previousSteps.size() - numberOfStepsBehind);
    }
}
