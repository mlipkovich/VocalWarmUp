package com.berniac.vocalwarmup.sequence;

import com.berniac.vocalwarmup.midi.MidiUtils;
import com.berniac.vocalwarmup.music.FixedStep;
import com.berniac.vocalwarmup.music.MusicalSymbol;
import com.berniac.vocalwarmup.music.Note;
import com.berniac.vocalwarmup.music.NoteValue;

import java.util.List;

import jp.kshoji.javax.sound.midi.EndSequenceEventListener;
import jp.kshoji.javax.sound.midi.InvalidMidiDataException;
import jp.kshoji.javax.sound.midi.MidiUnavailableException;
import jp.kshoji.javax.sound.midi.Sequence;
import jp.kshoji.javax.sound.midi.Sequencer;

/**
 * Created by Marina Gorlova on 02.12.2017.
 */
public class WarmUpPlayer implements Player {
    private WarmUpSequence warmUpSequence;
    private Sequencer sequencer;
    private WarmUp warmUp;
    public long currentTickPosition;
    private long sequenceLengthInTicks;
    private int semitonesToLowerBoundary;
    private int semitonesToUpperBoundary;
    private int playFrom;

    public WarmUpPlayer(WarmUpSequence warmUpSequence, Sequencer sequencer, WarmUp warmUp) {
        this.warmUpSequence = warmUpSequence;
        this.sequencer = sequencer;
        this.warmUp = warmUp;
        this.sequenceLengthInTicks = warmUpSequence.getSequence().getTickLength();
        this.currentTickPosition = MidiUtils.getMidiNote(warmUp.getStartingNote());
        this.setBoundaries();
        this.currentTickPosition = setStartTickPosition();
        this.playFrom = MidiUtils.getMidiNote(warmUp.getStartingNote());
        try {
            sequencer.setSequence(warmUpSequence.getSequence());
            sequencer.open();
            sequencer.addEndSequenceEventListener(new EndSequenceEventListener() {
                @Override
                public void sequenceEnd() {
                    //todo
                }
            });
            // TODO close sequencer somewhere
        } catch (InvalidMidiDataException|MidiUnavailableException e) {
            System.out.println(e);
        }
    }

    private int setStartTickPosition(){
        FixedStep step = (FixedStep) this.warmUp.getStep();
        int stepSize = step.getNextShift();
        int skipSteps = (MidiUtils.getMidiNote(warmUp.getStartingNote()) - warmUpSequence.getLowestTonic())/stepSize;
        return skipSteps*getStepSize();
    }

    public void onPlayFinished() {

    }

    @Override
    public void play() {
        setBoundaries();
        sequencer.setLoopStartPoint(currentTickPosition);
        sequencer.setLoopEndPoint(currentTickPosition + goUpToTick() - 1);
        sequencer.start();
    }

    @Override
    public void pause() {
        currentTickPosition = sequencer.getTickPosition();
        sequencer.stop();
    }

    @Override
    public void stop() {
        currentTickPosition = 0;
        sequencer.stop();
    }

    @Override
    public void openSettings() {

    }

    @Override
    public void openConstructor() {

    }

    private void goToStep(int stepNumber) {
        sequencer.stop();
        currentTickPosition = stepNumber*getStepSize();

        if (currentTickPosition >= sequenceLengthInTicks) {
            currentTickPosition = 0;
            return;
        }
        if (currentTickPosition < 0) {
            currentTickPosition = 0;
        }
        sequencer.start();
    }

    // the first step is zero
    private int getCurrentStep() {
        return (int) Math.floor(1.0*sequencer.getTickPosition()/getStepSize());
    }

    @Override
    public void repeatCurrentStep() {
        goToStep(getCurrentStep());
    }

    @Override
    public void previousStep() {
        goToStep(getCurrentStep() - 1);
    }

    @Override
    public void nextStep() {
        goToStep(getCurrentStep() + 1);
        System.out.println("next step");
    }

    @Override
    public void melodyOn() {

    }

    @Override
    public void melodyOff() {

    }

    @Override
    public void harmonyOn() {

    }

    @Override
    public void harmonyOff() {

    }

    @Override
    public void recordOn() {

    }

    @Override
    public void recordOff() {

    }

    @Override
    public void changeTemp() {

    }

    @Override
    public void showNoteVisualisation() {

    }

    @Override
    public void showLyrics() {

    }

    @Override
    public Sequence getSequence() {
        return warmUpSequence.getSequence();
    }

    public static int getLengthInTicks(List<MusicalSymbol> musicalSymbols) {
        int length = 0;
        for (MusicalSymbol symbol: musicalSymbols) {
            length += MidiUtils.getNoteValueInTicks(symbol.getNoteValue());
        }
        return length;
    }

    public int getStepSize() {
        return getLengthInTicks(warmUp.getMelody().getVoices().get(0).getMusicalSymbols())
                + warmUp.getPauseSize()* MidiUtils.getNoteValueInTicks(NoteValue.QUARTER);
    }

    public void setBoundaries() {
        // only with FixedStep warmUps
        List<MusicalSymbol> musicalSymbols = warmUp.getMelody().getVoices().get(0).getMusicalSymbols();
        int tonic = MidiUtils.getTonic();

        int semitonesToLowerBoundary = 0;
        int semitonesToUpperBoundary = 0;

        for (MusicalSymbol musicalSymbol : musicalSymbols) {
            if (musicalSymbol.isSounding()) {
                Note note = (Note) musicalSymbol;
                int semitonesToTonic = MidiUtils.getMidiNote(note.getNoteRegister()) - tonic;
                if (semitonesToTonic < semitonesToLowerBoundary) {
                    semitonesToLowerBoundary = semitonesToTonic;
                }
                if (semitonesToTonic > semitonesToUpperBoundary) {
                    semitonesToUpperBoundary = semitonesToTonic;
                }

            }
        }
        this.semitonesToLowerBoundary = semitonesToLowerBoundary;
        this.semitonesToUpperBoundary = semitonesToUpperBoundary;
    }

    public int goUpToTick() {
        FixedStep step = (FixedStep) warmUp.getStep();
        int stepSize = step.getNextShift();

        int availableSteps = (MidiUtils.getMidiNote(warmUp.getUpperNote())
                - playFrom - semitonesToUpperBoundary - 1)/stepSize;
        System.out.println("availableSteps " + availableSteps);
        System.out.println(MidiUtils.getMidiNote(warmUp.getUpperNote()));
        System.out.println(MidiUtils.getMidiNote(warmUp.getLowerNote()));
        System.out.println(MidiUtils.getMidiNote(warmUp.getStartingNote()));

        List<MusicalSymbol> musicalSymbols = warmUp.getMelody().getVoices().get(0).getMusicalSymbols();
        for (MusicalSymbol musicalSymbol : musicalSymbols) {
            Note note = (Note) musicalSymbol;
            System.out.print(" " + MidiUtils.getMidiNote(note.getNoteRegister()));
        }
         System.out.println("");
        return availableSteps*getStepSize();

//
//        int lowerNoteMidi = MidiUtils.getMidiNote(this.warmUp.getLowerNote());
//        int upperNoteMidi = MidiUtils.getMidiNote(this.warmUp.getUpperNote());
//
//        int toLowerAvailTonic = (lowerStart - lowerNoteMidi) % stepSize;
//        int lowerAvailTonic = 0;
//        if (toLowerAvailTonic >= 0) {
//            lowerAvailTonic = lowerNoteMidi + toLowerAvailTonic;
//        } else if (toLowerAvailTonic < 0) {
//            lowerAvailTonic = lowerNoteMidi + stepSize + toLowerAvailTonic;
//        }
//
//        int toUpperAvailTonic = (upperNoteMidi - upperStart) % stepSize;
//        int upperAvailTonic = 0;
//        if (toUpperAvailTonic >= 0) {
//            upperAvailTonic = upperNoteMidi - toUpperAvailTonic - stepSize;
//        } else if (toUpperAvailTonic < 0) {
//            upperAvailTonic = upperNoteMidi - 2*stepSize - toUpperAvailTonic;
//        }
//
//        if ((lowerNoteMidi <= lowerAvailTonic && lowerAvailTonic <= upperNoteMidi)
//                && (lowerNoteMidi <= upperAvailTonic && upperAvailTonic <= upperNoteMidi)) {
//            this.lowerBoundary = lowerAvailTonic;
//            this.upperBoundary = upperAvailTonic;
//        } else {
//            throw new IllegalStateException("Can't play any music");
//        }
    }
}
