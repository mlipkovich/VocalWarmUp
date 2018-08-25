package com.berniac.vocalwarmup.sequence;

import com.berniac.vocalwarmup.sequence.sequencer.StepSequencer;

/**
 * Created by Marina Gorlova on 02.12.2017.
 */
public class WarmUpPlayer implements Player {

    private StepSequencer sequencer;

    public WarmUpPlayer(StepSequencer sequencer, SequenceFinishedListener sequenceFinishedListener,
                        DirectionChangedListener directionChangedListener) {
        this.sequencer = sequencer;
        this.sequencer.setSequenceFinishedListener(sequenceFinishedListener);
        this.sequencer.setDirectionChangedListener(directionChangedListener);
    }

    @Override
    public void play() {
        if (!sequencer.isRunning()) {
            sequencer.run();
        }
        sequencer.play();
    }

    @Override
    public void pause() {
        if (sequencer.isRunning()) {
            sequencer.pause();
        }
    }

    @Override
    public void stop() {
        if (sequencer.isRunning()) {
            sequencer.stop();
        }
    }

    @Override
    public void repeatCurrentStep() {
        // TODO: Perhaps make these actions forbidden on UI
        if (sequencer.isRunning()) {
            sequencer.repeatCurrentStep();
        }
    }

    @Override
    public void changeDirection() {
        if (sequencer.isRunning()) {
            sequencer.changeDirection();
        }
    }

    @Override
    public void previousStep() {
        if (sequencer.isRunning()) {
            sequencer.previousStep();
        }
    }

    @Override
    public void nextStep() {
        if (sequencer.isRunning()) {
            sequencer.nextStep();
        }
    }

    @Override
    public void changeTempoFactor(float tempoFactor) {
        sequencer.setTempoFactor(tempoFactor);
    }

    @Override
    public void changeTempo(int tempoBpm) {
        sequencer.setTempo(tempoBpm);
    }

    @Override
    public void changeMelodyVolume(int volume) {
        sequencer.setMelodyVolume(volume);
    }

    @Override
    public void changeHarmonyVolume(int volume) {
        sequencer.setHarmonyVolume(volume);
    }

    @Override
    public void changeAdjustmentVolume(int volume) {
        sequencer.setAdjustmentVolume(volume);
    }

    @Override
    public void openSettings() {

    }

    @Override
    public void openConstructor() {

    }

    @Override
    public void recordOn() {

    }

    @Override
    public void recordOff() {

    }

    @Override
    public void showNoteVisualisation() {

    }

    @Override
    public void showLyrics() {

    }
}
