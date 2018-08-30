package com.berniac.vocalwarmup.sequence.sequencer;

import com.berniac.vocalwarmup.sequence.Direction;
import com.berniac.vocalwarmup.sequence.DirectionChangedListener;
import com.berniac.vocalwarmup.sequence.SequenceFinishedListener;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Mikhail Lipkovich on 8/15/2018.
 */
public class StepConsumerThread extends Thread {

    private volatile boolean isRunning;
    private volatile boolean isPaused;
    private volatile boolean isStepBackward;
    private volatile boolean isStepRepeat;
    private volatile boolean isStepRevert;

    private volatile int eventTonic;
    private volatile int eventBackwardTonic;
    private volatile Direction eventDirection;

    private StepConsumer consumer;
    private MidiReceiver receiver;
    private volatile SequenceFinishedListener sequenceFinishedListener;
    private volatile DirectionChangedListener directionChangedListener;

    private final Lock lock = new ReentrantLock();
    private final Condition unPaused = lock.newCondition();


    public StepConsumerThread(BlockingQueue<WarmUpStep> steps, MidiReceiver receiver) {
        this.receiver = receiver;
        this.consumer = new QueueStepConsumer(steps);

        this.isRunning = true;
        this.isPaused = false;
        this.isStepBackward = false;
    }

    @Override
    public void run() {
        WarmUpStep previousStep = null;
        Direction previousDirection = null;
        while (isRunning) {
            WarmUpStep step;
            try {
                step = getNewStepOrRepeat(previousStep);

                if (step == WarmUpStep.FINAL_STEP) {
                    System.out.println("WarmUp has finished");
                    if (sequenceFinishedListener != null) {
                        sequenceFinishedListener.onSequenceFinished();
                    }
                    isRunning = false;
                    continue;
                }

                eventTonic = step.getTonic();
                eventBackwardTonic = step.getBackwardTonic();
                eventDirection = step.getDirection();
                if (eventDirection != previousDirection) {
                    previousDirection = eventDirection;
                    directionChangedListener.onDirectionChanged(eventDirection);
                }

                Set<MidiEventShort> baseEvents = step.getBaseEvents();
                long tickPosition = processBaseEvents(0, baseEvents);

                Set<MidiEventShort> adjustmentEvents = getAdjustmentEvents(step);
                processAdjustmentEvents(tickPosition, adjustmentEvents);

                isStepRevert = false;
                previousStep = step;
            } catch (InterruptedException e) {
                receiver.muteAll();
                // might be switch to the next step or termination of the thread
            }
        }
        System.out.println("Consumer thread has been finished");
    }

    public void play() {
        isPaused = false;
        lock.lock();
        try {
            unPaused.signal();
        } finally {
            lock.unlock();
        }
    }

    public void pause() {
        isPaused = true;
        interrupt();
    }

    public void stopRunning() {
        isRunning = false;
        interrupt();
    }

    public void stepForward() {
        interrupt();
    }

    public void stepBackward() {
        isStepBackward = true;
        interrupt();
    }

    public void stepRepeat() {
        isStepRepeat = true;
    }

    public void stepRevert() {
        isStepRevert = true;
    }

    public int getEventTonic() {
        return eventTonic;
    }

    public int getEventBackwardTonic() {
        return eventBackwardTonic;
    }

    public Direction getEventDirection() {
        return eventDirection;
    }

    private WarmUpStep getNewStepOrRepeat(WarmUpStep previousStep) throws InterruptedException {
        if (isStepRepeat && previousStep != null) {
            isStepRepeat = false;
            return previousStep;
        }
        if (isStepBackward) {
            isStepBackward = false;
            return consumer.getPreviousStep();
        }
        return consumer.getNextStep();
    }

    private Set<MidiEventShort> getAdjustmentEvents(WarmUpStep step) {
        if (isStepRepeat) {
            return step.getAdjustmentRepeatEvents();
        }

        if (isStepRevert) {
            return step.getAdjustmentBackwardEvents();
        }

        return step.getAdjustmentForwardEvents();
    }

    private long processBaseEvents(long tickPosition, Set<MidiEventShort> events) throws InterruptedException {
        receiver.updateHarmonyVolume(true);
        for (MidiEventShort event : events) {
            receiver.updateHarmonyVolume(false);
            tickPosition = processEvent(tickPosition, event);
        }
        return tickPosition;
    }

    private long processAdjustmentEvents(long tickPosition, Set<MidiEventShort> events) throws InterruptedException {
        receiver.updateAdjustmentVolume(true);
        for (MidiEventShort event : events) {
            receiver.updateAdjustmentVolume(false);
            tickPosition = processEvent(tickPosition, event);
        }
        return tickPosition;
    }

    private long processEvent(long tickPosition, MidiEventShort event) throws InterruptedException{
        long sleepLength = receiver.timeRemainedBeforeEvent(event.getPosition(), tickPosition);
        if (sleepLength > 0) {
            try {
                Thread.sleep(sleepLength);
            } catch (InterruptedException e) {
                System.out.println("Sequencer sleep interrupted " + e);
                if (isPaused) {
                    receiver.muteAll();
                    waitOnPause();
                } else {
                    System.out.println("Sequencer interrupted not pause " + e);
                    throw e;
                }
            }
        }
        receiver.playEvent(event);
        return event.getPosition();
    }

    private void waitOnPause() throws InterruptedException {
        lock.lock();
        try {
            while (isPaused) {
                System.out.println("Sequencer interrupted pause");
                unPaused.await();
            }
        } finally {
            lock.unlock();
        }
    }

    public void setSequenceFinishedListener(SequenceFinishedListener sequenceFinishedListener) {
        this.sequenceFinishedListener = sequenceFinishedListener;
    }

    public void setDirectionChangedListener(DirectionChangedListener directionChangedListener) {
        this.directionChangedListener = directionChangedListener;
    }
}
