package com.berniac.vocalwarmup.sequence.sequencer;

import com.berniac.vocalwarmup.midi.MidiUtils;
import com.berniac.vocalwarmup.midi.SF2Sequencer;
import com.berniac.vocalwarmup.music.MusicalSymbol;
import com.berniac.vocalwarmup.music.Note;
import com.berniac.vocalwarmup.music.NoteRegister;
import com.berniac.vocalwarmup.music.NoteValue;
import com.berniac.vocalwarmup.music.Step;
import com.berniac.vocalwarmup.sequence.Direction;
import com.berniac.vocalwarmup.sequence.Instrument;
import com.berniac.vocalwarmup.sequence.OctaveShifts;
import com.berniac.vocalwarmup.sequence.Playable;
import com.berniac.vocalwarmup.sequence.WarmUp;
import com.berniac.vocalwarmup.sequence.WarmUpVoice;
import com.berniac.vocalwarmup.sequence.adjustment.Adjustment;
import com.berniac.vocalwarmup.sequence.adjustment.AdjustmentRules;
import com.berniac.vocalwarmup.sequence.adjustment.SilentAdjustmentRules;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import jp.kshoji.javax.sound.midi.InvalidMidiDataException;
import jp.kshoji.javax.sound.midi.MidiEvent;
import jp.kshoji.javax.sound.midi.MidiMessage;
import jp.kshoji.javax.sound.midi.MidiTrackSpecificEvent;
import jp.kshoji.javax.sound.midi.ShortMessage;

/**
 * Created by Mikhail Lipkovich on 6/01/2018.
 */
public class QueueStepProducer implements StepProducer {

    // TODO: Crescendo/diminuendo
    static final int VOLUME = 100;
    private BlockingQueue<WarmUpStep> steps;
    private WarmUp warmUp;

    public QueueStepProducer(BlockingQueue<WarmUpStep> steps, WarmUp warmUp) {
        this.steps = steps;
        this.warmUp = warmUp;
    }

    @Override
    public void run() {
        AdjustmentRules adjustmentRules = warmUp.getAdjustmentRules();
        Adjustment adjustment = new Adjustment(adjustmentRules, warmUp.getPauseSize());
        WarmUpVoice melodyVoice = warmUp.getMelody().getVoices().get(0);
        NoteValue lastMelodyNoteDuration = getLastNoteDuration(melodyVoice);
        Playable harmony = warmUp.getHarmony();
        if (harmony != null) {
            alignHarmonyDueToAdjustment(harmony, adjustmentRules, lastMelodyNoteDuration);
        }

        changePrograms(warmUp.getMelody(), warmUp.getHarmony());

        int lowestNoteInVoice = MidiUtils.getMidiNote(SequenceConstructor.getLowestNoteInVoice(
                warmUp.getMelody().getVoices().get(0)));
        int highestNoteInVoice = MidiUtils.getMidiNote(SequenceConstructor.getHighestNoteInVoice(
                warmUp.getMelody().getVoices().get(0)));

        int startingTonicMidi = MidiUtils.getMidiNote(warmUp.getStartingNote());
        int lowestTonicMidi = getLowestTonicInSequence(warmUp.getLowerNote(),
                lowestNoteInVoice, startingTonicMidi, warmUp.getStep().getNextShift());
        int highestTonicMidi = getHighestTonicInSequence(warmUp.getUpperNote(),
                highestNoteInVoice, startingTonicMidi, warmUp.getStep().getNextShift());


        TonicStateMachine stateMachine = new TonicStateMachine(lowestTonicMidi, startingTonicMidi,
                highestTonicMidi, warmUp.getStep(), warmUp.getDirections());

        while (!stateMachine.isFinished()) {

            WarmUpStep step = new WarmUpStep();
            long melodyStartTick = 0;
            long melodyEndTick = 0;
            long adjustmentStartTick;

            int currentTonicMidi = stateMachine.getCurrentTonic();
            int nextTonicMidi = stateMachine.getNextTonic();

            for (WarmUpVoice voice : warmUp.getMelody().getVoices()) {
                melodyEndTick = addStepVoice(step, currentTonicMidi, melodyStartTick, voice, MidiTrack.MELODY);
            }

            if (harmony != null) {
                for (WarmUpVoice voice : harmony.getVoices()) {
                    addStepVoice(step, currentTonicMidi, melodyStartTick, voice, MidiTrack.HARMONY);
                }
            }

            Playable adjustmentVoices = adjustment.getVoices(
                    MidiUtils.getNote(currentTonicMidi),
                    MidiUtils.getNote(nextTonicMidi));
            adjustmentStartTick = getAdjustmentStartTick(adjustmentRules,
                    melodyEndTick, lastMelodyNoteDuration);

            for (WarmUpVoice voice : adjustmentVoices.getVoices()) {
                addStepVoice(step, currentTonicMidi,adjustmentStartTick, voice, MidiTrack.ADJUSTMENT);
            }

            try {
                steps.put(step);
            } catch (InterruptedException e) {
                // TODO: Process later
                e.printStackTrace();
            }
        }
    }

    static void changePrograms(Playable melody, Playable harmony) {
        Set<Instrument> instruments = new HashSet<>();
        for (WarmUpVoice voice : melody.getVoices()) {
            instruments.add(voice.getInstrument());
        }
        if (harmony != null) {
            for (WarmUpVoice voice : harmony.getVoices()) {
                instruments.add(voice.getInstrument());
            }
        }
        SF2Sequencer.changePrograms(instruments);
    }

    static class TonicStateMachine {
        private int currentTonic;
        private int currentDirection;
        private boolean isFinished;

        private int lowestTonic;
        private int startingTonic;
        private int highestTonic;
        private Step step;
        private Direction[] directions;

        TonicStateMachine(int lowestTonic, int startingTonic, int highestTonic, Step step,
                          Direction[] directions) {
            this.step = step;
            this.lowestTonic = lowestTonic;
            this.startingTonic = startingTonic;
            this.highestTonic = highestTonic;
            this.directions = directions;

            this.currentDirection = 0;
            this.currentTonic = startingTonic;
            this.isFinished = false;
        }

        boolean isFinished() {
            return isFinished;
        }

        int getCurrentTonic() {
            return currentTonic;
        }

        int getNextTonic() {
            int nextTonic;
            switch (directions[currentDirection]) {
                case LOWER_TO_START:
                case LOWER_TO_UPPER:
                case START_TO_UPPER:
                    nextTonic = currentTonic + step.getNextShift();
                    break;
                case UPPER_TO_START:
                case UPPER_TO_LOWER:
                case START_TO_LOWER:
                    nextTonic = currentTonic - step.getNextShift();
                    break;
                default:
                    throw new IllegalStateException("Unknown direction " + directions[currentDirection]);

            }

            if (isDirectionFinished()) {
                currentDirection++;
                if (currentDirection == directions.length) {
                    isFinished = true;
                }
            }

            currentTonic = nextTonic;
            return currentTonic;
        }

        boolean isDirectionFinished() {
            Direction direction = directions[currentDirection];
            switch (direction) {
                case LOWER_TO_START:
                case UPPER_TO_START:
                    return currentTonic == startingTonic;
                case START_TO_UPPER:
                case LOWER_TO_UPPER:
                    return currentTonic == highestTonic;
                case START_TO_LOWER:
                case UPPER_TO_LOWER:
                    return currentTonic == lowestTonic;
                default:
                    throw new IllegalStateException("Unknown direction " + direction);
            }
        }
    }

    static int getLowestTonicInSequence(NoteRegister lowerNote, int lowestNoteMidiInVoice, int startingTonicMidi, int stepSize) {
        int maxDist = lowestNoteMidiInVoice - MidiUtils.getMidiNote(lowerNote);
        int maxDistFullStepsNumber = (int) Math.floor((float) maxDist / stepSize);
        int maxReachableDist = maxDistFullStepsNumber * stepSize;
        return startingTonicMidi - maxReachableDist;
    }

    static int getHighestTonicInSequence(NoteRegister upperNote, int highestNoteMidiInVoice, int startingTonicMidi, int stepSize) {
        int maxDist = MidiUtils.getMidiNote(upperNote) - highestNoteMidiInVoice;
        int maxDistFullStepsNumber = (int) Math.floor((float) maxDist / stepSize);
        int maxReachableDist = maxDistFullStepsNumber * stepSize;
        return startingTonicMidi + maxReachableDist;
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
                symbolIndex++; // TODO: --?
            }
        }
    }

    static NoteValue getLastNoteDuration(WarmUpVoice voice) {
        List<MusicalSymbol> melodySymbols = voice.getMusicalSymbols();
        MusicalSymbol lastMelodyNote = melodySymbols.get(melodySymbols.size() - 1);
        return lastMelodyNote.getNoteValue();
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

    private long addStepVoice(WarmUpStep step, int currentTonicMidi, long position,
                              WarmUpVoice voice, MidiTrack midiTrack) {
        int channel = SF2Sequencer.getChannel(voice.getInstrument());
        int octaveShift = getOctaveShift(voice.getOctaveShifts(), currentTonicMidi);

        for (MusicalSymbol symbol : voice.getMusicalSymbols()) {
            int note = MidiUtils.transpose(((Note) symbol).getNoteRegister(), currentTonicMidi, octaveShift);
            long duration = MidiUtils.getNoteValueInTicks(symbol.getNoteValue());

            try {
                MidiMessage midiMessage = new ShortMessage(ShortMessage.NOTE_ON, channel, note, VOLUME);
                MidiEvent midiEvent = new MidiEvent(midiMessage, position);
                MidiTrackSpecificEvent midiVoiceEvent = new MidiTrackSpecificEvent(midiEvent, midiTrack.index);

                step.add(midiVoiceEvent);
                midiMessage = new ShortMessage(ShortMessage.NOTE_OFF, channel, note, VOLUME);
                midiEvent = new MidiEvent(midiMessage, position + duration);
                midiVoiceEvent = new MidiTrackSpecificEvent(midiEvent, midiTrack.index);
                step.add(midiVoiceEvent);
            } catch (InvalidMidiDataException ignored) {
                throw new IllegalStateException("Failed to create midi event from tonic " + note +
                        " with channel " + channel + " and volume " + VOLUME);
            }

            position += duration;
        }
        return position;
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