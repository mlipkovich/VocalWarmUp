package com.berniac.vocalwarmup.sequence.sequencer;

import com.berniac.vocalwarmup.midi.MidiUtils;
import com.berniac.vocalwarmup.music.NoteValue;
import com.berniac.vocalwarmup.sequence.Direction;
import com.berniac.vocalwarmup.sequence.WarmUp;

import java.util.Set;

import jp.kshoji.javax.sound.midi.InvalidMidiDataException;
import jp.kshoji.javax.sound.midi.MidiEvent;
import jp.kshoji.javax.sound.midi.MidiTrackSpecificEvent;
import jp.kshoji.javax.sound.midi.Receiver;
import jp.kshoji.javax.sound.midi.ShortMessage;


/**
 * Created by Mikhail Lipkovich on 6/01/2018.
 */
public class StepSequencer {
    private ConsumerThread consumerThread;
    private ProducerThread producerThread;

    public StepSequencer(StepConsumer consumer, StepProducer producer, Receiver receiver) {

        this.consumerThread = new ConsumerThread(consumer, receiver);
        this.consumerThread.start();

        this.producerThread = new ProducerThread(producer);
        this.producerThread.start();
    }

    public void play() {
        consumerThread.play();
    }

    public void pause() {
        consumerThread.pause();
    }

    public void nextStep() {
        consumerThread.stepForward();
    }

    public void previousStep() {
        consumerThread.stepBackward();
    }

    public void muteMelody() {
        consumerThread.muteMelody();
    }

    public void unMuteMelody() {
        consumerThread.unMuteMelody();
    }

    public void muteHarmony() {
        consumerThread.muteHarmony();
    }

    public void unMuteHarmony() {
        consumerThread.unMuteHarmony();
    }

    public void setTempoFactor(float tempoFactor) {
        consumerThread.setTempoFactor(tempoFactor);
    }

    public void setTempo(int tempoBpm) {
        consumerThread.setTempo(tempoBpm);
    }

    public void repeatCurrentStep() {
        consumerThread.stepRepeat();
    }

    public void changeDirection() {
        producerThread.stopProducing();
        System.out.println("Interrupted producer method");
        producerThread.clean();

        consumerThread.stepRevert();
        int tonic = consumerThread.getEventBackwardTonic();
        Direction direction = consumerThread.getEventDirection();

        producerThread.changeDirection(tonic, direction);
        producerThread.restoreProducing();
    }


    private static class ProducerThread extends Thread {

        private volatile boolean isOpen;
        private volatile boolean isRunning;
        private volatile boolean isInterrupted;

        private StepProducer producer;

        public ProducerThread(StepProducer producer) {
            this.producer = producer;
            this.isOpen = true;
            this.isRunning = true;
            this.isInterrupted = false;
        }


        @Override
        public void run() {
            while (isOpen) {
                while (!isRunning && isOpen) {
                    System.out.println("Producer: Waiting for being running");
                    synchronized (this) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            isInterrupted = true;
                            System.out.println("Producer " + e);
                        }
                    }
                    System.out.println("Producer: continue running");
                }

                if (!isRunning || !isOpen) {
                    return;
                }

                try {
                    producer.generateSteps();
                } catch (InterruptedException e) {
                    isInterrupted = true;
                    System.out.println("Interrupted producer");
                    synchronized (this) {
                        notify();
                    }
                }
            }
        }

        public void clean() {
            producer.cleanGenerated();
        }

        public void changeDirection(int tonic, Direction direction) {
            producer.changeDirection(tonic, direction);
        }

        public void stopProducing() {
            isRunning = false;
            interrupt();
            while (!isInterrupted) {
                synchronized (this) {
                    try {
                        wait(100);
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted waiting interruption");
                    }
                }
            }
            isInterrupted = false;
        }

        public void restoreProducing() {
            isRunning = true;
            synchronized (this) {
                notify();
            }
        }
    }


    private static class ConsumerThread extends Thread {

        private static final int DEFAULT_BPM = 120;

        private volatile boolean isRunning;
        private volatile boolean isOpen;
        private volatile boolean isPaused;
        private volatile boolean isStepSkipped;
        private volatile boolean isStepBackward;
        private volatile boolean isStepRepeat;
        private volatile boolean isStepRevert;
        private volatile boolean isMelodyMuted;
        private volatile boolean isHarmonyMuted;

        private volatile int eventTonic;
        private volatile int eventBackwardTonic;
        private volatile Direction eventDirection;

        private volatile float tempoFactor;
        private volatile int tempoBpm;

        private StepConsumer consumer;
        private Receiver receiver;

        public ConsumerThread(StepConsumer consumer, Receiver receiver) {
            this.receiver = receiver;
            this.consumer = consumer;
            this.isOpen = true;
            this.isRunning = false;
            this.isPaused = false;
            this.isStepSkipped = false;
            this.isStepBackward = false;
            this.isMelodyMuted = false;
            this.isHarmonyMuted = false;

            this.tempoFactor = 1;
            this.tempoBpm = DEFAULT_BPM;
        }

        public void muteMelody() {
            isMelodyMuted = true;
        }

        public void unMuteMelody() {
            isMelodyMuted = false;
        }

        public void muteHarmony() {
            isHarmonyMuted = true;
        }

        public void unMuteHarmony() {
            isHarmonyMuted = false;
        }

        public void play() {
            isRunning = true;
            isPaused = false;
            synchronized (this) {
                notify();
            }
        }

        public void pause() {
            isRunning = false; // TODO: not sure if needed
            isPaused = true;
            interrupt();
        }

        public void stepForward() {
            isStepSkipped = true;
            interrupt();
        }

        public void stepBackward() {
            isStepSkipped = true;
            isStepBackward = true;
            interrupt();
        }

        public void stepRepeat() {
            isStepRepeat = true;
        }

        public void stepRevert() {
            isStepRevert = true;
        }

        public void setTempoFactor(float tempoFactor) {
            this.tempoFactor = tempoFactor;
        }


        public void setTempo(int tempoBpm) {
            this.tempoBpm = tempoBpm;
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

        @Override
        public void run() {
            WarmUpStep step = null;
            isStepRepeat = false;
            while (isOpen) {
                while (!isRunning && isOpen) {
                    System.out.println("Sequencer: Waiting for being running");
                    synchronized (this) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            System.out.println("Sequencer " + e);
                        }
                    }
                }

                if (!isRunning || !isOpen) {
                    return;
                }

                try {
                    if (!isStepRepeat || step == null) {
                        if (isStepBackward) {
                            isStepBackward = false;
                            step = consumer.getPreviousStep();
                        } else {
                            step = consumer.getNextStep();
                        }
                    } else {
                        isStepRepeat = false;
                    }
                } catch (InterruptedException ignored) {
                    // e.g. pressed next before the first note
                    continue;
                }

                eventTonic = step.getTonic();
                eventBackwardTonic = step.getBackwardTonic();
                System.out.println("Playing tonic " + eventTonic);
                eventDirection = step.getDirection();

                Set<MidiEvent> baseEvents = step.getBaseEvents();
                Set<MidiEvent> adjustmentEvents = step.getAdjustmentForwardEvents();
                long tickPosition = 0;
                try {
                    tickPosition = processEvents(tickPosition, baseEvents);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted from base events " + e);
                    if (isStepSkipped) {
                        isStepSkipped = false;
                        System.out.println("Sequencer interrupted base skip " + e);
                        continue;
                    } else {
                        System.out.println("Unknown interruption base reason " + e);
                        Thread.currentThread().interrupt();
                    }
                }

                if (isStepRepeat) {
                    adjustmentEvents = step.getAdjustmentRepeatEvents();
                } else if (isStepRevert) {
                    adjustmentEvents = step.getAdjustmentBackwardEvents();
                    isStepRevert = false;
                }

                try {
                    processEvents(tickPosition, adjustmentEvents);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted from adjustment events " + e);
                    if (isStepSkipped) {
                        isStepSkipped = false;
                        System.out.println("Sequencer interrupted adjustment skip " + e);
                        continue;
                    } else {
                        System.out.println("Unknown interruption adjustment reason " + e);
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        private long processEvents(long tickPosition, Set<MidiEvent> events) throws InterruptedException {
            for (MidiEvent event : events) {
                if (event instanceof MidiTrackSpecificEvent) {
                    int currentEventTrack = ((MidiTrackSpecificEvent) event).getTrackIndex();

                    // Allow to switch off muted sounds
                    if (((ShortMessage)event.getMessage()).getCommand() != ShortMessage.NOTE_OFF) {
                        if (isMelodyMuted && QueueStepProducer.MidiTrack.MELODY.getIndex() == currentEventTrack) {
                            continue;
                        }

                        if (isHarmonyMuted && QueueStepProducer.MidiTrack.HARMONY.getIndex() == currentEventTrack) {
                            continue;
                        }
                    }
                }

                // TODO: Remove debug logging below
                long sleepLength = (long) ((1.0f / getTicksPerMicrosecond()) * (event.getTick() - tickPosition) / 1000f / tempoFactor);
                if (sleepLength > 0) {
                    try {
                        Thread.sleep(sleepLength);
                    } catch (InterruptedException e) {
                        System.out.println("Sequencer sleep interrupted " + e);
                        if (isPaused) {
                            while (isPaused) {
                                System.out.println("Sequencer interrupted pause");
                                muteAllSounds();
                                synchronized (this) {
                                    try {
                                        wait();
                                    } catch (InterruptedException e1) {
                                        System.out.println("Sequencer interrupted unpause");
                                    }
                                }
                            }
                        } else {
                            System.out.println("Sequencer interrupted not pause " + e);
                            throw e;
                        }
                    }
                }
                tickPosition = event.getTick();
                receiver.send(event.getMessage(), 0);
            }
            return tickPosition;
        }

        float getTicksPerMicrosecond() {
            int ticksPerBeat = MidiUtils.getNoteValueInTicks(NoteValue.QUARTER);
            float beatsPerSecond = tempoBpm / 60f;
            float microsecondPerSecond = 1000000.0f;

            return (beatsPerSecond * ticksPerBeat) / microsecondPerSecond;
        }

        void muteAllSounds() {
            try {
                // TODO: Do it for all channels
                receiver.send(new ShortMessage(0xb0, 0x7B, 0),
                        -1);
                receiver.send(new ShortMessage(0xb1, 0x7B, 0),
                        -1);
            } catch (InvalidMidiDataException ignored) {
                // should never happen
            }
        }
    }
}
