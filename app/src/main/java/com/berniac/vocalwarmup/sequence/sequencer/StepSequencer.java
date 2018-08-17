package com.berniac.vocalwarmup.sequence.sequencer;

import com.berniac.vocalwarmup.midi.SF2Sequencer;
import com.berniac.vocalwarmup.sequence.Accompaniment;
import com.berniac.vocalwarmup.sequence.Direction;
import com.berniac.vocalwarmup.sequence.DirectionChangedListener;
import com.berniac.vocalwarmup.sequence.Instrument;
import com.berniac.vocalwarmup.sequence.SequenceFinishedListener;
import com.berniac.vocalwarmup.sequence.WarmUp;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import jp.kshoji.javax.sound.midi.Receiver;


/**
 * Created by Mikhail Lipkovich on 6/01/2018.
 */
public class StepSequencer {

    private static final int QUEUE_CAPACITY = 10;
    private BlockingQueue<WarmUpStep> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

    private final WarmUp warmUp;
    private MidiReceiver midiReceiver;

    private StepConsumerThread consumerThread;
    private StepProducerThread producerThread;

    private SequenceFinishedListener sequenceFinishedListener;
    private DirectionChangedListener directionChangedListener;

    private volatile boolean isRunning;

    public StepSequencer(WarmUp warmUp) {
        // TODO: Init channel volumes and deal with channel-voice mapping
        changePrograms(warmUp.getAccompaniment().getVoices().values());

        Receiver receiver = SF2Sequencer.getReceiver();
        this.midiReceiver = new MidiReceiver(receiver);
        this.warmUp = warmUp;
    }

    private static void changePrograms(Collection<Accompaniment.Voice> voices) {
        Set<Instrument> instruments = new HashSet<>();
        instruments.add(Instrument.MELODIC_VOICE);
        for (Accompaniment.Voice voice : voices) {
            instruments.add(voice.getInstrument());
        }
        SF2Sequencer.changePrograms(instruments);
    }

    public void setSequenceFinishedListener(SequenceFinishedListener sequenceFinishedListener) {
        this.sequenceFinishedListener = sequenceFinishedListener;
    }

    public void setDirectionChangedListener(DirectionChangedListener directionChangedListener) {
        this.directionChangedListener = directionChangedListener;
    }

    public void run() {
        queue.clear();
        producerThread = new StepProducerThread(queue, warmUp);

        consumerThread = new StepConsumerThread(queue, midiReceiver);
        consumerThread.setSequenceFinishedListener(new SequenceFinishedListener() {
            @Override
            public void onSequenceFinished() {
                sequenceFinishedListener.onSequenceFinished();
                producerThread.stopRunning();
                isRunning = false;
            }
        });
        consumerThread.setDirectionChangedListener(directionChangedListener);

        producerThread.start();
        consumerThread.start();

        isRunning = true;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void play() {
        consumerThread.play();
    }

    public void pause() {
        consumerThread.pause();
    }

    public void stop() {
        System.out.println("Stopping all threads");
        consumerThread.stopRunning();
        producerThread.stopRunning();
    }

    public void nextStep() {
        consumerThread.stepForward();
    }

    public void previousStep() {
        consumerThread.stepBackward();
    }

    public void repeatCurrentStep() {
        consumerThread.stepRepeat();
    }

    public void changeDirection() {
        consumerThread.stepRevert();
        int tonic = consumerThread.getEventBackwardTonic();
        Direction direction = consumerThread.getEventDirection();

        producerThread.changeDirection(tonic, direction);
    }


    public void setTempo(int tempoBpm) {
        midiReceiver.setTempo(tempoBpm);
    }

    public void setTempoFactor(float tempoFactor) {
        midiReceiver.setTempoFactor(tempoFactor);
    }

    public void setMelodyVolume(int volume) {
        midiReceiver.updateMelodyVolume(volume);
    }

    public void setHarmonyVolume(int volume) {
        midiReceiver.setHarmonyVolume(volume);
    }

    public void setAdjustmentVolume(int volume) {
        midiReceiver.setAdjustmentVolume(volume);
    }
}
