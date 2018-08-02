package com.berniac.vocalwarmup.sequence;

import com.berniac.vocalwarmup.sequence.sequencer.StepSequencer;

/**
 * Created by Marina Gorlova on 02.12.2017.
 */
public class WarmUpPlayer implements Player {

    private StepSequencer sequencer;

    public WarmUpPlayer(StepSequencer sequencer, SequenceFinishedListener sequenceFinishedListener) {
        this.sequencer = sequencer;
        this.sequencer.setSequenceFinishedListener(sequenceFinishedListener);
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
        sequencer.pause();
    }

    @Override
    public void stop() {
        sequencer.pause();
    }

    @Override
    public void repeatCurrentStep() {
        sequencer.repeatCurrentStep();
    }

    @Override
    public void changeDirection() {
        sequencer.changeDirection();
    }

    @Override
    public void previousStep() {
        sequencer.previousStep();
    }

    @Override
    public void nextStep() {
        sequencer.nextStep();
    }

    @Override
    public void melodyOn() {
        sequencer.unMuteMelody();
    }

    @Override
    public void melodyOff() {
        sequencer.muteMelody();
    }

    @Override
    public void harmonyOn() {
        sequencer.unMuteHarmony();
    }

    @Override
    public void harmonyOff() {
        sequencer.muteHarmony();
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
