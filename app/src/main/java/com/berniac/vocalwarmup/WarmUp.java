package com.berniac.vocalwarmup;

/**
 * Created by Marina on 13.11.2017.
 */

public class WarmUp {

    private byte lowerNote;
    private byte upperNote;
    private byte startingNote;
    private byte currentNote;
    private Direction[] directions;
    private WarmUpVoice melody;
    private boolean runMelody;
    private WarmUpVoice[] harmony;
    private boolean runHarmony;
    private Step step;
    private boolean runMetronome;
    private int tempoInBPM;
    private int pauseSize;
    private boolean patternChanged;

    public byte getLowerNote() {
        return lowerNote;
    }

    public void setLowerNote(byte lowerNote) {
        this.lowerNote = lowerNote;
        this.patternChanged = true;
    }

    public byte getUpperNote() {
        return upperNote;
    }

    public void setUpperNote(byte upperNote) {
        this.upperNote = upperNote;
        this.patternChanged = true;
    }

    public byte getStartingNote() {
        return startingNote;
    }

    public void setStartingNote(byte startingNote) {
        this.startingNote = startingNote;
        this.patternChanged = true;
    }

    public byte getCurrentNote() {
        return currentNote;
    }

    public void setCurrentNote(byte currentNote) {
        this.currentNote = currentNote;
    }

    public Direction[] getDirections() {
        return directions;
    }

    public void setDirections(Direction[] directions) {
        this.directions = directions;
        this.patternChanged = true;
    }

    public WarmUpVoice getMelody() {
        return melody;
    }

    public void setMelody(WarmUpVoice melody) {
        this.melody = melody;
        this.patternChanged = true;
    }

    public boolean getRunMelody() {
        return runMelody;
    }

    public void setRunMelody(boolean runMelody) {
        this.runMelody = runMelody;
    }

    public WarmUpVoice[] getHarmony() {
        return harmony;
    }

    public void setHarmony(WarmUpVoice[] harmony) {
        this.harmony = harmony;
        this.patternChanged = true;
    }

    public boolean getRunHarmony() {
        return runHarmony;
    }

    public void setRunHarmony(boolean runHarmony) {
        this.runHarmony = runHarmony;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
        this.patternChanged = true;
    }

    public boolean getRunMetronome() {
        return runMetronome;
    }

    public void setRunMetronome(boolean runMetronome) {
        this.runMetronome = runMetronome;
    }

    public int getTempoInBPM() {
        return tempoInBPM;
    }

    public void setTempoInBPM(int tempoInBPM) {
        this.tempoInBPM = tempoInBPM;
        this.patternChanged = true;
    }

    public int getPauseSize() {
        return pauseSize;
    }

    public void setPauseSize(int pauseSize) {
        this.pauseSize = pauseSize;
        this.patternChanged = true;
    }

    public boolean getPatternChanged() {
        return patternChanged;
    }
}
