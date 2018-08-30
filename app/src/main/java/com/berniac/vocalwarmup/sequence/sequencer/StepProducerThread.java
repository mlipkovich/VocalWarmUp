package com.berniac.vocalwarmup.sequence.sequencer;

import com.berniac.vocalwarmup.midi.MidiUtils;
import com.berniac.vocalwarmup.music.MusicalSymbol;
import com.berniac.vocalwarmup.music.Note;
import com.berniac.vocalwarmup.music.NoteRegister;
import com.berniac.vocalwarmup.music.Step;
import com.berniac.vocalwarmup.sequence.Direction;
import com.berniac.vocalwarmup.sequence.WarmUp;
import com.berniac.vocalwarmup.sequence.WarmUpVoice;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Mikhail Lipkovich on 8/14/2018.
 */
public class StepProducerThread extends Thread{

    private volatile boolean isRunning;
    private WarmUp warmUp;
    private StepProducer stepProducer;
    private final TonicStateMachine stateMachine = new TonicStateMachine();
    private BlockingQueue<WarmUpStep> steps;

    private final Lock lock = new ReentrantLock();
    private final Condition stoppedGeneration  = lock.newCondition();
    private final Condition generate = lock.newCondition();

    private volatile boolean hasGenerationStopped;
    private volatile boolean shouldGenerate;

    public StepProducerThread(BlockingQueue<WarmUpStep> steps, WarmUp warmUp) {
        this.steps = steps;
        this.warmUp = warmUp;
        this.stepProducer = new StepProducer(warmUp);
        this.isRunning = true;
        this.shouldGenerate = true;
        this.hasGenerationStopped = false;

        initStateMachine(warmUp.getLowerNote(),
                warmUp.getUpperNote(),
                MidiUtils.getMidiNote(warmUp.getStartingNote()),
                warmUp.getDirections());
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                if (stateMachine.isFinished()) {
                    System.out.println("Producer put the end of steps");
                    steps.put(WarmUpStep.FINAL_STEP);
                    stateMachine.waitHasMoreData();
                }
                WarmUpStep step = generateStep();
                steps.put(step);
            } catch (InterruptedException e) {
                if (!isRunning) {
                    System.out.println("Producer thread has been finished");
                    return;
                }
                signalGenerationStopped();
                waitForGenerationAllowed();
            }
        }
        System.out.println("Producer thread has been finished");
    }

    public void stopRunning() {
        isRunning = false;
        interrupt();
    }

    public WarmUpStep generateStep()  {
        int currentTonicMidi = stateMachine.getCurrentTonic();
        Direction direction = stateMachine.getCurrentDirection();
        int nextTonicMidi = stateMachine.getNextTonicAndUpdate();

        return stepProducer.generateStep(currentTonicMidi, nextTonicMidi, direction);
    }

    public void changeDirection(int startingTonicMidi, Direction previousDirection) {
        stopGeneration();
        cleanGenerated();
        updateStateMachine(startingTonicMidi, previousDirection);
        restoreGeneration();
    }

    private void waitForGenerationAllowed() {
        System.out.println("Waiting for generation allowed");
        lock.lock();
        try {
            while (!shouldGenerate) {
                try {
                    generate.await();
                    hasGenerationStopped = false;
                } catch (InterruptedException e) {
                    System.out.println("Interrupted generate await");
                    break;
                }
            }
        } finally {
            lock.unlock();
        }
        System.out.println("Finished waiting for generation allowed");
    }

    private void signalGenerationStopped() {
        System.out.println("Signal generation stopped");
        lock.lock();
        try {
            hasGenerationStopped = true;
            stoppedGeneration.signal();
        } finally {
            lock.unlock();
        }
        System.out.println("Finished signal generation stopped");
    }

    private void cleanGenerated() {
        steps.clear();
    }

    private void initStateMachine(NoteRegister lowerNote, NoteRegister upperNote,
                                              int startingTonicMidi, Direction[] directions) {
        int lowestNoteInVoice = MidiUtils.getMidiNote(StepProducerThread.getLowestNoteInVoice(
                warmUp.getMelody().getVoices().get(0)));
        int highestNoteInVoice = MidiUtils.getMidiNote(StepProducerThread.getHighestNoteInVoice(
                warmUp.getMelody().getVoices().get(0)));

        int lowestTonicMidi = getLowestTonicInSequence(lowerNote,
                lowestNoteInVoice, startingTonicMidi, warmUp.getStep().getNextShift());
        int highestTonicMidi = getHighestTonicInSequence(upperNote,
                highestNoteInVoice, startingTonicMidi, warmUp.getStep().getNextShift());

        stateMachine.init(lowestTonicMidi, startingTonicMidi,
                highestTonicMidi, warmUp.getStep(), directions);
    }

    private void updateStateMachine(int startingTonicMidi, Direction previousDirection) {
        System.out.println("Changing direction");
        Direction newDirection;
        switch (previousDirection) {
            case LOWER_TO_START:
            case LOWER_TO_UPPER:
            case START_TO_UPPER:
                newDirection = Direction.START_TO_LOWER;
                break;
            case UPPER_TO_START:
            case UPPER_TO_LOWER:
            case START_TO_LOWER:
                newDirection = Direction.START_TO_UPPER;
                break;
            default:
                throw new IllegalStateException("Unknown direction " + previousDirection);
        }

        initStateMachine(NoteRegister.LOWEST_NOTE, NoteRegister.HIGHEST_NOTE,
                startingTonicMidi, new Direction[]{newDirection});
    }

    private void stopGeneration() {
        System.out.println("Stopping generations");
        lock.lock();
        try {
            shouldGenerate = false;
            interrupt();
            while (!hasGenerationStopped) {
                try {
                    stoppedGeneration.await();
                } catch (InterruptedException e) {
                    System.out.println("Interrupted stopped generation await");
                }
            }
        } finally {
            lock.unlock();
        }
        System.out.println("Finished stopping generations");
    }

    private void restoreGeneration() {
        System.out.println("Restoring generation");
        lock.lock();
        try {
            shouldGenerate = true;
            generate.signal();
        } finally {
            lock.unlock();
        }
        System.out.println("Finished restoring generation");
    }

    private static int getLowestTonicInSequence(NoteRegister lowerNote, int lowestNoteMidiInVoice, int startingTonicMidi, int stepSize) {
        int maxDist = lowestNoteMidiInVoice - MidiUtils.getMidiNote(lowerNote);
        int maxDistFullStepsNumber = (int) Math.floor((float) maxDist / stepSize);
        int maxReachableDist = maxDistFullStepsNumber * stepSize;
        return startingTonicMidi - maxReachableDist;
    }

    private static int getHighestTonicInSequence(NoteRegister upperNote, int highestNoteMidiInVoice, int startingTonicMidi, int stepSize) {
        int maxDist = MidiUtils.getMidiNote(upperNote) - highestNoteMidiInVoice;
        int maxDistFullStepsNumber = (int) Math.floor((float) maxDist / stepSize);
        int maxReachableDist = maxDistFullStepsNumber * stepSize;
        return startingTonicMidi + maxReachableDist;
    }

    private static NoteRegister getLowestNoteInVoice(WarmUpVoice voice) {
        List<MusicalSymbol> symbols = voice.getMusicalSymbols();
        int lowestNoteMidi = Integer.MAX_VALUE;
        Note lowestSymbol = null;
        for (MusicalSymbol symbol : symbols) {
            if (symbol.isSounding()) {
                Note note = (Note)symbol;
                int noteMidi = MidiUtils.getMidiNote(note.getNoteRegister());
                if (noteMidi <= lowestNoteMidi) {
                    lowestNoteMidi = noteMidi;
                    lowestSymbol = note;
                }
            }
        }
        if (lowestSymbol == null) {
            throw new IllegalStateException("There is no notes in melody " + symbols);
        }
        return lowestSymbol.getNoteRegister();
    }

    private static NoteRegister getHighestNoteInVoice(WarmUpVoice voice) {
        List<MusicalSymbol> symbols = voice.getMusicalSymbols();
        int highestNoteMidi = 0;
        Note highestSymbol = null;
        for (MusicalSymbol symbol : symbols) {
            if (symbol.isSounding()) {
                Note note = (Note)symbol;
                int noteMidi = MidiUtils.getMidiNote(note.getNoteRegister());
                if (noteMidi >= highestNoteMidi) {
                    highestNoteMidi = noteMidi;
                    highestSymbol = note;
                }
            }
        }
        if (highestSymbol== null) {
            throw new IllegalStateException("There is no notes in melody " + symbols);
        }
        return highestSymbol.getNoteRegister();
    }

    private static class TonicStateMachine {
        private int currentTonic;
        private int currentDirection;
        private boolean isFinished;

        private int lowestTonic;
        private int startingTonic;
        private int highestTonic;
        private Step step;
        private Direction[] directions;

        private final Lock lock = new ReentrantLock();
        private final Condition hasMoreData = lock.newCondition();

        TonicStateMachine() {
        }

        void init(int lowestTonic, int startingTonic, int highestTonic, Step step,
                              Direction[] directions) {
            this.step = step;
            this.lowestTonic = lowestTonic;
            this.startingTonic = startingTonic;
            this.highestTonic = highestTonic;
            this.directions = directions;

            this.currentDirection = 0;
            this.currentTonic = startingTonic;

            lock.lock();
            try {
                System.out.println("Signaling has more data");
                this.isFinished = false;
                hasMoreData.signal();
                System.out.println("Finished signaling has more data");
            } finally {
                lock.unlock();
            }
        }

        boolean isFinished() {
            return isFinished;
        }

        int getCurrentTonic() {
            return currentTonic;
        }

        Direction getCurrentDirection() {
            return directions[currentDirection];
        }

        void waitHasMoreData() throws InterruptedException {
            if (isFinished()) {
                lock.lock();
                try {
                    while (isFinished()) {
                        System.out.println("Waiting for next tonic");
                        hasMoreData.await();
                        System.out.println("Finished waiting for next tonic");
                    }
                } finally {
                    lock.unlock();
                }
            }
        }

        int getNextTonicAndUpdate() {
            int nextTonic;
            switch (directions[currentDirection]) {
                case LOWER_TO_START:
                case LOWER_TO_UPPER:
                case START_TO_UPPER:
                    nextTonic = currentTonic + step.getNextShift();
                    break;
                case UPPER_TO_START:
                case UPPER_TO_LOWER:
                case START_TO_LOWER:
                    nextTonic = currentTonic - step.getNextShift();
                    break;
                default:
                    throw new IllegalStateException("Unknown direction " + directions[currentDirection]);
            }

            if (isDirectionFinished()) {
                currentDirection++;
                if (currentDirection == directions.length) {
                    isFinished = true;
                    // Finish with modulation into itself
                    nextTonic = currentTonic;
                }
            }

            currentTonic = nextTonic;
            return currentTonic;
        }

        boolean isDirectionFinished() {
            Direction direction = directions[currentDirection];
            switch (direction) {
                case LOWER_TO_START:
                case UPPER_TO_START:
                    return currentTonic == startingTonic;
                case START_TO_UPPER:
                case LOWER_TO_UPPER:
                    return currentTonic == highestTonic;
                case START_TO_LOWER:
                case UPPER_TO_LOWER:
                    return currentTonic == lowestTonic;
                default:
                    throw new IllegalStateException("Unknown direction " + direction);
            }
        }
    }
}
