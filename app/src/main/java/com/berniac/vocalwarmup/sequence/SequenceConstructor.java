package com.berniac.vocalwarmup.sequence;

import com.berniac.vocalwarmup.midi.MidiUtils;
import com.berniac.vocalwarmup.midi.SF2Sequencer;
import com.berniac.vocalwarmup.music.MusicalSymbol;
import com.berniac.vocalwarmup.music.Note;
import com.berniac.vocalwarmup.music.NoteRegister;
import com.berniac.vocalwarmup.music.NoteValue;
import com.berniac.vocalwarmup.music.Step;
import com.berniac.vocalwarmup.sequence.adjustment.Adjustment;
import com.berniac.vocalwarmup.sequence.adjustment.AdjustmentRules;
import com.berniac.vocalwarmup.sequence.adjustment.SilentAdjustmentRules;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.kshoji.javax.sound.midi.InvalidMidiDataException;
import jp.kshoji.javax.sound.midi.MidiEvent;
import jp.kshoji.javax.sound.midi.MidiMessage;
import jp.kshoji.javax.sound.midi.MidiTrackSpecificEvent;
import jp.kshoji.javax.sound.midi.Sequence;
import jp.kshoji.javax.sound.midi.ShortMessage;
import jp.kshoji.javax.sound.midi.Track;

/**
 * Created by Mikhail Lipkovich on 11/10/2017.
 */
public class SequenceConstructor {

    private static final String TAG = SequenceConstructor.class.getSimpleName();
    private static final int TICKS_IN_QUARTER_NOTE =
            MidiUtils.getNoteValueInTicks(NoteValue.QUARTER);

    // TODO: Crescendo/diminuendo
    static final int VOLUME = 100;

    private SequenceConstructor(){}


    public static WarmUpSequence construct(WarmUp warmUp) throws Exception {

        changePrograms(warmUp.getMelody(), warmUp.getHarmony());
        Sequence sequence = new Sequence(Sequence.PPQ, TICKS_IN_QUARTER_NOTE, MidiTrack.values().length);
        WarmUpVoice melodyVoice = warmUp.getMelody().getVoices().get(0);
        Step step = warmUp.getStep();
        int startingTonicMidi = MidiUtils.getMidiNote(warmUp.getStartingNote());
        int lowestNoteInVoice = MidiUtils.getMidiNote(getLowestNoteInVoice(melodyVoice));
        int highestNoteInVoice = MidiUtils.getMidiNote(getHighestNoteInVoice(melodyVoice));
        int lowestTonicMidi = getLowestTonicInSequence(lowestNoteInVoice, startingTonicMidi, step.getNextShift());
        int highestTonicMidi = getHighestTonicInSequence(highestNoteInVoice, startingTonicMidi, step.getNextShift());

        AdjustmentRules adjustmentRules = warmUp.getAdjustmentRules();
        Adjustment adjustment = new Adjustment(adjustmentRules, warmUp.getPauseSize());
        NoteValue lastMelodyNoteDuration = getLastNoteDuration(melodyVoice);
        Playable harmony = warmUp.getHarmony();
        if (harmony != null) {
            alignHarmonyDueToAdjustment(harmony, adjustmentRules, lastMelodyNoteDuration);
        }

        long melodyStartTick = 0;
        long melodyEndTick;
        long adjustmentStartTick;
        long adjustmentEndTick;

        TonicStateMachine tonicStateMachine =
                new TonicStateMachine(lowestTonicMidi, highestTonicMidi, warmUp.getStep());

        while (!tonicStateMachine.isFinished()) {

            int currentTonicMidi = tonicStateMachine.getCurrentTonic();

            melodyEndTick = addStepPlayable(sequence, MidiTrack.MELODY, warmUp.getMelody(),
                    currentTonicMidi, melodyStartTick);
            if (harmony != null) {
                addStepPlayable(sequence, MidiTrack.HARMONY, warmUp.getHarmony(),
                        currentTonicMidi, melodyStartTick);
            }
            int nextTonicMidi = tonicStateMachine.getNextTonic();
            Playable adjustmentVoices = adjustment.getVoices(
                    MidiUtils.getNote(currentTonicMidi),
                    MidiUtils.getNote(nextTonicMidi));
            adjustmentStartTick = getAdjustmentStartTick(adjustmentRules,
                    melodyEndTick, lastMelodyNoteDuration);
            adjustmentEndTick = addStepPlayable(sequence, MidiTrack.ADJUSTMENT, adjustmentVoices,
                    currentTonicMidi, adjustmentStartTick);
            melodyStartTick = adjustmentEndTick;
            // TODO: Add metronome and lyrics
        }

        return new WarmUpSequence(sequence, lowestTonicMidi, highestTonicMidi);
    }

    static void changePrograms(Playable melody, Playable harmony) {
        Set<Instrument> instruments = new HashSet<>();
        for (WarmUpVoice voice : melody.getVoices()) {
            instruments.add(voice.getInstrument());
        }
        for (WarmUpVoice voice : harmony.getVoices()) {
            instruments.add(voice.getInstrument());
        }
        SF2Sequencer.changePrograms(instruments);
    }

    static class TonicStateMachine {
        private int currentTonic;
        private State state;

        private int lowestTonic;
        private int highestTonic;
        private Step step;

        private enum State {
            LOWEST_TO_HIGHEST,
            HIGHEST_TO_LOWEST,
            RANDOM_DIRECTION,
            FINISHED
        }

        TonicStateMachine(int lowestTonic, int highestTonic, Step step) {
            this.step = step;
            this.lowestTonic = lowestTonic;
            this.highestTonic = highestTonic;
            this.state = State.LOWEST_TO_HIGHEST;
            this.currentTonic = lowestTonic;
        }

        boolean isFinished() {
            return state == State.FINISHED;
        }

        int getCurrentTonic() {
            return currentTonic;
        }

        int getNextTonic() {
            switch (state) {
                case LOWEST_TO_HIGHEST:
                    currentTonic = currentTonic + step.getNextShift();
                    if (currentTonic == highestTonic) {
                        state = State.HIGHEST_TO_LOWEST;
                    }
                    break;
                case HIGHEST_TO_LOWEST:
                    currentTonic = currentTonic - step.getNextShift();
                    if (currentTonic == lowestTonic) {
                        state = State.FINISHED;
                    }
                    break;
                case FINISHED:
                    throw new IllegalStateException("State machine is in FINISHED state");
            }
            return currentTonic;
        }
    }

    static long getAdjustmentStartTick(AdjustmentRules adjustmentRules,
                                       long melodyEndTick, NoteValue lastMelodyNoteDuration) {
        long adjustmentStartTick = melodyEndTick;
        if (adjustmentRules instanceof SilentAdjustmentRules) {
            return adjustmentStartTick;
        }

        int lastNoteTicks = MidiUtils.getNoteValueInTicks(lastMelodyNoteDuration);
        adjustmentStartTick -= lastNoteTicks;
        return adjustmentStartTick;
    }

    static NoteRegister getLowestNoteInVoice(WarmUpVoice voice) {
        List<MusicalSymbol> symbols = voice.getMusicalSymbols();
        int lowestNoteMidi = Integer.MAX_VALUE;
        Note lowestSymbol = null;
        for (MusicalSymbol symbol : symbols) {
            if (symbol.isSounding()) {
                Note note = (Note)symbol;
                int noteMidi = MidiUtils.getMidiNote(note.getNoteRegister());
                if (noteMidi <= lowestNoteMidi) {
                    lowestNoteMidi = noteMidi;
                    lowestSymbol = note;
                }
            }
        }
        if (lowestSymbol == null) {
            throw new IllegalStateException("There is no notes in voice " + symbols);
        }
        return lowestSymbol.getNoteRegister();
    }

    static NoteRegister getHighestNoteInVoice(WarmUpVoice voice) {
        List<MusicalSymbol> symbols = voice.getMusicalSymbols();
        int highestNoteMidi = 0;
        Note highestSymbol = null;
        for (MusicalSymbol symbol : symbols) {
            if (symbol.isSounding()) {
                Note note = (Note)symbol;
                int noteMidi = MidiUtils.getMidiNote(note.getNoteRegister());
                if (noteMidi >= highestNoteMidi) {
                    highestNoteMidi = noteMidi;
                    highestSymbol = note;
                }
            }
        }
        if (highestSymbol== null) {
            throw new IllegalStateException("There is no notes in voice " + symbols);
        }
        return highestSymbol.getNoteRegister();
    }


    static int getLowestTonicInSequence(int lowestNoteMidiInVoice, int startingTonicMidi, int stepSize) {
        int maxDist = lowestNoteMidiInVoice - MidiUtils.getMidiNote(NoteRegister.LOWEST_NOTE);
        int maxDistFullStepsNumber = (int) Math.floor((float) maxDist / stepSize);
        int maxReachableDist = maxDistFullStepsNumber * stepSize;
        return startingTonicMidi - maxReachableDist;
    }

    static int getHighestTonicInSequence(int highestNoteMidiInVoice, int startingTonicMidi, int stepSize) {
        int maxDist = MidiUtils.getMidiNote(NoteRegister.HIGHEST_NOTE) - highestNoteMidiInVoice;
        int maxDistFullStepsNumber = (int) Math.floor((float) maxDist / stepSize);
        int maxReachableDist = maxDistFullStepsNumber * stepSize;
        return startingTonicMidi + maxReachableDist;
    }

    static NoteValue getLastNoteDuration(WarmUpVoice voice) {
        List<MusicalSymbol> melodySymbols = voice.getMusicalSymbols();
        MusicalSymbol lastMelodyNote = melodySymbols.get(melodySymbols.size() - 1);
        return lastMelodyNote.getNoteValue();
    }

    private static void alignHarmonyDueToAdjustment(Playable harmony, AdjustmentRules adjustmentRules,
                                                    NoteValue lastMelodyNoteDuration) {
        if (adjustmentRules instanceof SilentAdjustmentRules) {
            return;
        }

        float lastMelodyNoteQuartersCount = lastMelodyNoteDuration.getNumberOfQuarters();
        for (WarmUpVoice harmonyVoice : harmony.getVoices()) {
            List<MusicalSymbol> voiceSymbols = harmonyVoice.getMusicalSymbols();
            int symbolIndex = voiceSymbols.size() - 1;
            float erasedNotesQuartersCount = 0;
            while (erasedNotesQuartersCount < lastMelodyNoteQuartersCount) {
                erasedNotesQuartersCount +=
                        voiceSymbols.get(symbolIndex).getNoteValue().getNumberOfQuarters();
                voiceSymbols.remove(symbolIndex);
                symbolIndex++;
            }
        }
    }

    private static long addStepPlayable(Sequence sequence, MidiTrack midiTrack, Playable playable,
                                       int tonic, long position) throws InvalidMidiDataException {
        Track track = sequence.getTracks()[midiTrack.index];
        long stepEndPosition = 0;
        List<WarmUpVoice> voices = playable.getVoices();
        for (WarmUpVoice voice : voices) {
            stepEndPosition = addStepVoice(track, midiTrack, voice, tonic, position);
        }
        return stepEndPosition;
    }

    static long addStepVoice(Track track, MidiTrack midiTrack, WarmUpVoice voice,
                             int tonic, long previousTick) throws InvalidMidiDataException {

        int channel = SF2Sequencer.getChannel(voice.getInstrument());

        int octaveShift = getOctaveShift(voice.getOctaveShifts(), tonic);
        for (MusicalSymbol symbol : voice.getMusicalSymbols()) {
            long duration = MidiUtils.getNoteValueInTicks(symbol.getNoteValue());
            int midiNote;
            if (!symbol.isSounding()) {
                midiNote = MidiUtils.getMidiNote(NoteRegister.HIGHEST_NOTE);
            } else {
                midiNote = MidiUtils.transpose(((Note) symbol).getNoteRegister(), tonic, octaveShift);
            }
//            if (symbol.isSounding()) {
//                midiNote = MidiUtils.transpose(((Note) symbol).getNoteRegister(), tonic, octaveShift);
                addNote(track, midiTrack, channel, midiNote, duration, previousTick);
//            }
            previousTick += duration;
        }
        return previousTick;
    }

    static int getOctaveShift(OctaveShifts shifts, int tonicMidi) {
        List<OctaveShifts.BoundaryNote> lowerBoundaries = shifts.getLowerBoundaries();
        for (int i = lowerBoundaries.size() - 1; i >= 0; i--) {
            OctaveShifts.BoundaryNote boundaryNote = lowerBoundaries.get(i);
            int boundaryMidi = MidiUtils.getMidiNote(boundaryNote.getBoundary());
            if (tonicMidi <= boundaryMidi) {
                return boundaryNote.getShift();
            }
        }

        List<OctaveShifts.BoundaryNote> upperBoundaries = shifts.getUpperBoundaries();
        for (int i = upperBoundaries.size() - 1; i >= 0; i--) {
            OctaveShifts.BoundaryNote boundaryNote = upperBoundaries.get(i);
            int boundaryMidi = MidiUtils.getMidiNote(boundaryNote.getBoundary());
            if (tonicMidi >= boundaryMidi) {
                return boundaryNote.getShift();
            }
        }

        return 0;
    }

    static void addNote(Track track, MidiTrack midiTrack, int channel, int note,
                        long duration, long position) throws InvalidMidiDataException {
        MidiMessage midiMessage = new ShortMessage(ShortMessage.NOTE_ON, channel, note, VOLUME);
        MidiEvent midiEvent = new MidiEvent(midiMessage, position);
        MidiTrackSpecificEvent midiVoiceEvent = new MidiTrackSpecificEvent(midiEvent, midiTrack.index);
        track.add(midiVoiceEvent);
        midiMessage = new ShortMessage(ShortMessage.NOTE_OFF, channel, note, VOLUME);
        midiEvent = new MidiEvent(midiMessage, position + duration);
        midiVoiceEvent = new MidiTrackSpecificEvent(midiEvent, midiTrack.index);
        track.add(midiVoiceEvent);
    }

    enum MidiTrack {
        MELODY(0),
        ADJUSTMENT(1),
        METRONOME(2),
        HARMONY(3),
        LYRICS(4);

        private int index;
        MidiTrack(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }
}
