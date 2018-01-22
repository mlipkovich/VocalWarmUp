package com.berniac.vocalwarmup.sequence;

import com.berniac.vocalwarmup.midi.MidiUtils;
import com.berniac.vocalwarmup.music.MusicalSymbol;
import com.berniac.vocalwarmup.music.Note;
import com.berniac.vocalwarmup.music.NoteRegister;
import com.berniac.vocalwarmup.music.NoteSymbol;
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
    private Sequence sequence;
    private Sequencer sequencer;
    private WarmUp warmUp;
    public long currentTickPosition;
    private long sequenceLengthInTicks;
    private int lowerBoundary;
    private int upperBoundary;
    private int lowerTonicFromSequence;
    private int upperTonicFromSequence;

    public WarmUpPlayer(Sequence sequence, Sequencer sequencer, WarmUp warmUp) {
        this.sequence = sequence;
        this.sequencer = sequencer;
        this.warmUp = warmUp;
        this.sequenceLengthInTicks = sequence.getTickLength();
        this.setBoundaries();
        try {
            sequencer.setSequence(sequence);
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

    public void onPlayFinished() {

    }

    @Override
    public void play() {
        sequencer.setLoopStartPoint(currentTickPosition);
        //TODO: sequencer.setLoopEndPoint();
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
        List<MusicalSymbol> musicalSymbols = warmUp.getMelody().getVoices().get(0).getMusicalSymbols();
        int tonic = MidiUtils.getTonic();
        int semitonesToLowerBoundary = 0;
        int semitonesToUpperBoundary = 0;

        for (MusicalSymbol musicalSymbol: musicalSymbols) {
            if (musicalSymbol.isSounding()) {
                Note note = (Note) musicalSymbol;
                int semitonesToTonic = tonic - MidiUtils.getMidiNote(note.getNoteRegister());
                if (semitonesToTonic < semitonesToLowerBoundary) {
                    semitonesToLowerBoundary = semitonesToTonic;
                }
                if (semitonesToTonic > semitonesToUpperBoundary) {
                    semitonesToUpperBoundary = semitonesToTonic;
                }

            }
        }
    }
}
