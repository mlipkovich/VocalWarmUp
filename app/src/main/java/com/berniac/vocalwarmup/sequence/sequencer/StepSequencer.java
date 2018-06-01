package com.berniac.vocalwarmup.sequence.sequencer;

import com.berniac.vocalwarmup.midi.MidiUtils;
import com.berniac.vocalwarmup.music.NoteValue;

import jp.kshoji.javax.sound.midi.InvalidMidiDataException;
import jp.kshoji.javax.sound.midi.MidiEvent;
import jp.kshoji.javax.sound.midi.MidiTrackSpecificEvent;
import jp.kshoji.javax.sound.midi.Receiver;
import jp.kshoji.javax.sound.midi.ShortMessage;


/**
 * Created by Mikhail Lipkovich on 6/01/2018.
 */
public class StepSequencer {
    private StepConsumer consumer;
    private ConsumerThread consumerThread;
    private volatile boolean isRunning;

    public StepSequencer(StepConsumer consumer, StepProducer producer, Receiver receiver) {
        this.consumerThread = new ConsumerThread(consumer, receiver);
        this.consumerThread.start();

        new Thread(producer).start();
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

    private static class ConsumerThread extends Thread {

        private static final int DEFAULT_BPM = 120;

        private volatile boolean isRunning;
        private volatile boolean isOpen;
        private volatile boolean isPaused;
        private volatile boolean isStepSkipped;
        private volatile boolean isStepBackward;
        private volatile boolean isMelodyMuted;
        private volatile boolean isHarmonyMuted;

        private volatile float tempoFactor;
        private volatile int tempoBPM;

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
            this.tempoBPM = DEFAULT_BPM;
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

        public void setTempoFactor(float tempoFactor) {
            this.tempoFactor = tempoFactor;
        }

        @Override
        public void run() {
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

                WarmUpStep step;
                try {
                    if (isStepBackward) {
                        isStepBackward = false;
                        step = consumer.getPreviousStep();
                    } else {
                        step = consumer.getNextStep();
                    }
                } catch (InterruptedException ignored) {
                    // e.g. pressed next before the first note
                    continue;
                }

                long tickPosition = 0;
                for (MidiEvent event : step.events) {
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
                            System.out.println("Sequencer sleep interrupted" + e);
                            if (isPaused) {
                                while (isPaused) {
                                    System.out.println("Sequencer interrupted pause");
                                    muteAllSounds();
                                    synchronized (this) {
                                        try {
                                            wait();
                                        } catch (InterruptedException e1) {
                                            System.out.println("Sequencer interrupted pause");
                                        }
                                    }
                                }
                            } else if (isStepSkipped) {
                                isStepSkipped = false;
                                System.out.println("Sequencer interrupted skip " + e);
                                break;
                            } else {
                                System.out.println("Sequencer interrupted other " + e);
                                return;
                            }
                        }
                    }
                    tickPosition = event.getTick();
                    receiver.send(event.getMessage(), 0);
                }
            }
        }

        float getTicksPerMicrosecond() {
            int ticksPerBeat = MidiUtils.getNoteValueInTicks(NoteValue.QUARTER);
            float beatsPerSecond = tempoBPM / 60f;
            float microsecondPerSecond = 1000000.0f;

            return (beatsPerSecond * ticksPerBeat) / microsecondPerSecond;
        }

        void muteAllSounds() {
            try {
                receiver.send(new ShortMessage(ShortMessage.CONTROL_CHANGE, 0x7B, 0),
                        -1);
            } catch (InvalidMidiDataException ignored) {
                // should never happen
            }
        }
    }
}
