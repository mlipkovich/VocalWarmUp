package com.berniac.vocalwarmup.sequence;

import com.berniac.vocalwarmup.sequence.sequencer.StepSequencer;

import jp.kshoji.javax.sound.midi.Sequence;

/**
 * Created by Marina Gorlova on 02.12.2017.
 */
public class WarmUpPlayer implements Player {

    private StepSequencer sequencer;

    public WarmUpPlayer(StepSequencer sequencer) {
        this.sequencer = sequencer;
    }

    @Override
    public void play() {
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
    public void changeTempo(float tempoFactor) {
        sequencer.setTempoFactor(tempoFactor);
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
