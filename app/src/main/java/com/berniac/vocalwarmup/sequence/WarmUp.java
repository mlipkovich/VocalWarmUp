package com.berniac.vocalwarmup.sequence;

import com.berniac.vocalwarmup.music.MusicalSymbol;
import com.berniac.vocalwarmup.music.Note;
import com.berniac.vocalwarmup.music.NoteRegister;
import com.berniac.vocalwarmup.music.NoteSymbol;
import com.berniac.vocalwarmup.music.Step;
import com.berniac.vocalwarmup.sequence.adjustment.Adjustment;
import com.berniac.vocalwarmup.sequence.adjustment.AdjustmentRules;

import static com.berniac.vocalwarmup.music.NoteRegister.semitonesBetween;

/**
 * Created by Marina Gorlova on 13.11.2017.
 */
public class WarmUp {

    private NoteRegister lowerNote;
    private NoteRegister upperNote;
    private NoteRegister startingNote;
    private int currentNote;       // TODO: Replace with NoteRegister?
    private Direction[] directions;
    private Playable melody;
    private boolean runMelody;
    private Playable harmony;
    private AdjustmentRules adjustmentRules;
    private boolean runHarmony;
    private Step step;
    private boolean runMetronome;
    private int tempoInBPM;
    private int pauseSize;
    private boolean patternChanged;

    public NoteRegister getLowerNote() {
        return lowerNote;
    }

    public void setLowerNote(NoteRegister lowerNote) {
        this.lowerNote = lowerNote;
        this.patternChanged = true;
    }

    public NoteRegister getUpperNote() {
        return upperNote;
    }

    public void setUpperNote(NoteRegister upperNote) {
        this.upperNote = upperNote;
        this.patternChanged = true;
    }

    public NoteRegister getStartingNote() {
        return startingNote;
    }

    public void setStartingNote(NoteRegister startingNote) {
        this.startingNote = startingNote;
        this.patternChanged = true;
    }

    public int getCurrentNote() {
        return currentNote;
    }

    public void setCurrentNote(int currentNote) {
        this.currentNote = currentNote;
    }

    public Direction[] getDirections() {
        return directions;
    }

    public void setDirections(Direction[] directions) {
        this.directions = directions;
        this.patternChanged = true;
    }

    public Playable getMelody() {
        return melody;
    }

    public void setMelody(Melody melody) {
        this.melody = melody;
        this.patternChanged = true;
    }

    public boolean getRunMelody() {
        return runMelody;
    }

    public void setRunMelody(boolean runMelody) {
        this.runMelody = runMelody;
    }

    public Playable getHarmony() {
        return harmony;
    }

    public void setHarmony(Harmony harmony) {
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

    public AdjustmentRules getAdjustmentRules() {
        return adjustmentRules;
    }

    public void setAdjustmentRules(AdjustmentRules adjustmentRules) {
        this.adjustmentRules = adjustmentRules;
    }
}
