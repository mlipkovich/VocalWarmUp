package com.berniac.vocalwarmup.sequence;

import com.berniac.vocalwarmup.midi.MidiUtils;
import com.berniac.vocalwarmup.music.MusicalSymbol;
import com.berniac.vocalwarmup.music.Note;
import com.berniac.vocalwarmup.music.NoteRegister;
import com.berniac.vocalwarmup.music.NoteValue;
import com.berniac.vocalwarmup.music.Step;
import com.berniac.vocalwarmup.sequence.adjustment.Adjustment;
import com.berniac.vocalwarmup.sequence.adjustment.AdjustmentRules;
import com.berniac.vocalwarmup.sequence.adjustment.SilentAdjustmentRules;

import java.util.List;

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


    public static Sequence construct(WarmUp warmUp) throws Exception {

        System.out.println("Creating sequence for warm up " + warmUp);
        Sequence sequence = new Sequence(Sequence.PPQ, TICKS_IN_QUARTER_NOTE, MidiTrack.values().length);

        WarmUpVoice melodyVoice = warmUp.getMelody().getVoices().get(0);
        int startingNoteMidi = MidiUtils.getMidiNote(warmUp.getStartingNote());
        int lowestVoiceNoteMidi = MidiUtils.getMidiNote(getLowestNoteInVoice(melodyVoice));
        int highestVoiceNoteMidi = MidiUtils.getMidiNote(getHighestNoteInVoice(melodyVoice));

        AdjustmentRules adjustmentRules = warmUp.getAdjustmentRules();
        Adjustment adjustment = new Adjustment(adjustmentRules, warmUp.getPauseSize());
        Playable harmony = warmUp.getHarmony();
        if (harmony != null) {
            alignHarmonyDueToAdjustment(harmony, warmUp.getMelody(), adjustmentRules);
        }

        long melodyStartTick = 0;
        long melodyEndTick;
        long harmonyEndTick;
        long adjustmentStartTick;
        long adjustmentEndTick;

        TonicStateMachine tonicStateMachine =
                TonicStateMachine.create(lowestVoiceNoteMidi, highestVoiceNoteMidi, startingNoteMidi, warmUp.getStep());

        while (!tonicStateMachine.isFinished()) {

            int currentTonicMidi = tonicStateMachine.getCurrentTonic();

            melodyEndTick = addStepPlayable(sequence, MidiTrack.MELODY, warmUp.getMelody(),
                    currentTonicMidi, melodyStartTick);

            if (harmony != null) {
                harmonyEndTick = addStepPlayable(sequence, MidiTrack.HARMONY, warmUp.getHarmony(),
                        currentTonicMidi, melodyStartTick);
                adjustmentStartTick = harmonyEndTick;
            } else {
                int lastNoteTicks =
                        MidiUtils.getNoteValueInTicks(getLastNoteDuration(melodyVoice));
                adjustmentStartTick = melodyEndTick - lastNoteTicks;
            }

            int nextTonicMidi = tonicStateMachine.getNextTonic();
            Playable adjustmentVoices = adjustment.getVoices(
                    MidiUtils.getNote(currentTonicMidi),
                    MidiUtils.getNote(nextTonicMidi));
            adjustmentEndTick = addStepPlayable(sequence, MidiTrack.ADJUSTMENT, adjustmentVoices,
                    currentTonicMidi, adjustmentStartTick);

            melodyStartTick = adjustmentEndTick;

            // TODO: Add metronome and lyrics
        }

        return sequence;
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

        // used mostly for test convenience
        TonicStateMachine(int lowestTonic, int highestTonic, Step step) {
            this.step = step;
            this.lowestTonic = lowestTonic;
            this.highestTonic = highestTonic;
            this.state = State.LOWEST_TO_HIGHEST;
            this.currentTonic = lowestTonic;
        }

        static TonicStateMachine create(int lowestNoteInVoice, int highestNoteInVoice, int startingTonic, Step step) {
            // TODO: Support random step
            int lowestNote = getLowestNoteMidiInSequence(lowestNoteInVoice, startingTonic, step.getNextShift());
            int highestNote = getHighestNoteMidiInSequence(highestNoteInVoice, startingTonic, step.getNextShift());
            return new TonicStateMachine(lowestNote, highestNote, step);
        }

        static int getLowestNoteMidiInSequence(int lowestNoteMidiInVoice, int startingTonicMidi, int stepSize) {
            int maxDist = lowestNoteMidiInVoice - MidiUtils.getMidiNote(NoteRegister.LOWEST_NOTE);
            int maxDistFullStepsNumber = (int) Math.floor((float) maxDist / stepSize);
            int maxReachableDist = maxDistFullStepsNumber * stepSize;
            return startingTonicMidi - maxReachableDist;
        }

        static int getHighestNoteMidiInSequence(int highestNoteMidiInVoice, int startingTonicMidi, int stepSize) {
            int maxDist = MidiUtils.getMidiNote(NoteRegister.HIGHEST_NOTE) - highestNoteMidiInVoice;
            int maxDistFullStepsNumber = (int) Math.floor((float) maxDist / stepSize);
            int maxReachableDist = maxDistFullStepsNumber * stepSize;
            return startingTonicMidi + maxReachableDist;
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

    static NoteValue getLastNoteDuration(WarmUpVoice voice) {
        List<MusicalSymbol> melodySymbols = voice.getMusicalSymbols();
        MusicalSymbol lastMelodyNote = melodySymbols.get(melodySymbols.size() - 1);
        return lastMelodyNote.getNoteValue();
    }

    private static void alignHarmonyDueToAdjustment(Playable harmony, Playable melody,
                                                    AdjustmentRules adjustmentRules) {
        if (adjustmentRules instanceof SilentAdjustmentRules) {
            return;
        }

        WarmUpVoice melodyVoice = melody.getVoices().get(0);
        float lastMelodyNoteQuartersCount = getLastNoteDuration(melodyVoice).getNumberOfQuarters();
        for (WarmUpVoice harmonyVoice : harmony.getVoices()) {
            List<MusicalSymbol> voiceSymbols = harmonyVoice.getMusicalSymbols();
            int symbolIndex = 0;
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

        // TODO: Define channel by voice.getInstrument()
        int channel = 0;

        for (MusicalSymbol symbol : voice.getMusicalSymbols()) {
            long duration = MidiUtils.getNoteValueInTicks(symbol.getNoteValue());
            if (symbol.isSounding()) {
                int midiNote = MidiUtils.transpose(((Note) symbol).getNoteRegister(), tonic);
                addNote(track, midiTrack, channel, midiNote, duration, previousTick);
            }
            previousTick += duration;
        }
        return previousTick;
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
