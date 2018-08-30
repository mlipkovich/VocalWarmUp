package com.berniac.vocalwarmup.sequence.sequencer;

import com.berniac.vocalwarmup.midi.MidiUtils;
import com.berniac.vocalwarmup.music.MusicalSymbol;
import com.berniac.vocalwarmup.music.Note;
import com.berniac.vocalwarmup.music.NoteRegister;
import com.berniac.vocalwarmup.music.NoteSymbol;
import com.berniac.vocalwarmup.music.NoteValue;
import com.berniac.vocalwarmup.sequence.Accompaniment;
import com.berniac.vocalwarmup.sequence.Direction;
import com.berniac.vocalwarmup.sequence.Harmony;
import com.berniac.vocalwarmup.sequence.Instrument;
import com.berniac.vocalwarmup.sequence.OctaveShifts;
import com.berniac.vocalwarmup.sequence.Playable;
import com.berniac.vocalwarmup.sequence.WarmUp;
import com.berniac.vocalwarmup.sequence.WarmUpVoice;
import com.berniac.vocalwarmup.sequence.adjustment.Adjustment;
import com.berniac.vocalwarmup.sequence.adjustment.AdjustmentRules;
import com.berniac.vocalwarmup.sequence.adjustment.SilentAdjustmentRules;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import jp.kshoji.javax.sound.midi.ShortMessage;

/**
 * Created by Mikhail Lipkovich on 6/01/2018.
 */
public class StepProducer {

    private WarmUp warmUp;
    private Map<Integer, Accompaniment.Voice> voices;
    private Playable harmony;
    private AdjustmentRules adjustmentRules;
    private Adjustment adjustment;
    private NoteValue lastMelodyNoteDuration;

    public StepProducer(WarmUp warmUp) {
        this.warmUp = warmUp;
        this.voices = warmUp.getAccompaniment().getVoices();
        // TODO: Add adjustment index to warm up
        this.adjustmentRules = warmUp.getAccompaniment().getAdjustments().get(0);
        this.adjustment = new Adjustment(adjustmentRules, warmUp.getPauseSize());

        WarmUpVoice melodyVoice = warmUp.getMelody().getVoices().get(0);
        this.lastMelodyNoteDuration = getLastNoteDuration(melodyVoice);

        Harmony harmony = warmUp.getAccompaniment().getHarmony();
        if (harmony != null) {
            this.harmony = alignHarmonyDueToAdjustment(harmony, adjustmentRules, lastMelodyNoteDuration);
        }
    }

    public WarmUpStep generateStep(int currentTonicMidi, int nextTonicMidi, Direction direction) {

        int forwardTonicMidi = nextTonicMidi;
        int backwardTonicMidi = currentTonicMidi - (nextTonicMidi - currentTonicMidi);

        WarmUpStep step = new WarmUpStep(currentTonicMidi, forwardTonicMidi, backwardTonicMidi, direction);

        long melodyStartTick = 0;
        long melodyEndTick = addStepPlayable(step.getBaseEvents(), currentTonicMidi, melodyStartTick,
                warmUp.getMelody(), MidiTrack.MELODY);

        if (harmony != null) {
            addStepPlayable(step.getBaseEvents(), currentTonicMidi, melodyStartTick, harmony, MidiTrack.HARMONY);
        }

        long adjustmentStartTick = getAdjustmentStartTick(adjustmentRules, melodyEndTick, lastMelodyNoteDuration);

        long adjustmentEndTick = addAdjustment(step.getAdjustmentForwardEvents(), currentTonicMidi,
                forwardTonicMidi, adjustmentStartTick, adjustment);
        addAdjustment(step.getAdjustmentBackwardEvents(), currentTonicMidi,
                backwardTonicMidi, adjustmentStartTick, adjustment);
        addAdjustment(step.getAdjustmentRepeatEvents(), currentTonicMidi,
                currentTonicMidi, adjustmentStartTick, adjustment);

        MidiEventShort lastMelodyNoteOffEvent = ((TreeSet<MidiEventShort>) step.getBaseEvents()).pollLast();
        step.getAdjustmentForwardEvents().add(lastMelodyNoteOffEvent);
        step.getAdjustmentBackwardEvents().add(lastMelodyNoteOffEvent);
        step.getAdjustmentRepeatEvents().add(lastMelodyNoteOffEvent);

        return step;
    }

    private long addAdjustment(Set<MidiEventShort> events, int fromTonic, int toTonic, long position, Adjustment adjustment) {
        Playable adjustmentVoices = adjustment.getVoices(
                MidiUtils.getNote(fromTonic),
                MidiUtils.getNote(toTonic));
//        System.out.println("Adjustment " + adjustmentVoices);
        return addStepPlayable(events, fromTonic, position, adjustmentVoices, MidiTrack.ADJUSTMENT);
    }

    private static Harmony alignHarmonyDueToAdjustment(Harmony harmony, AdjustmentRules adjustmentRules,
                                                    NoteValue lastMelodyNoteDuration) {
        if (adjustmentRules instanceof SilentAdjustmentRules) {
            return harmony;
        }

        Harmony alignedHarmony = new Harmony(harmony);

        float lastMelodyNoteQuartersCount = lastMelodyNoteDuration.getNumberOfQuarters();
        for (WarmUpVoice harmonyVoice : alignedHarmony.getVoices()) {
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

        return alignedHarmony;
    }

    private static NoteValue getLastNoteDuration(WarmUpVoice voice) {
        List<MusicalSymbol> melodySymbols = voice.getMusicalSymbols();
        MusicalSymbol lastMelodyNote = melodySymbols.get(melodySymbols.size() - 1);
        return lastMelodyNote.getNoteValue();
    }

    private static long getAdjustmentStartTick(AdjustmentRules adjustmentRules,
                                       long melodyEndTick, NoteValue lastMelodyNoteDuration) {
        long adjustmentStartTick = melodyEndTick;
        if (adjustmentRules instanceof SilentAdjustmentRules) {
            return adjustmentStartTick;
        }

        int lastNoteTicks = MidiUtils.getNoteValueInTicks(lastMelodyNoteDuration);
        adjustmentStartTick -= lastNoteTicks;
        return adjustmentStartTick;
    }

    private long addStepPlayable(Set<MidiEventShort> events, int currentTonicMidi, long position,
                              Playable playable, MidiTrack midiTrack) {
        long voiceEndPosition = position;
        for (int i = 0; i < playable.getVoices().size(); i++) {
            WarmUpVoice warmUpVoice = playable.getVoices().get(i);
//            System.out.println("Voice " + warmUpVoice + " for " + midiTrack);
            Accompaniment.Voice voice = voices.get(warmUpVoice.getVoiceNumber());
//            System.out.println(voice);
            voiceEndPosition = addStepVoice(events, currentTonicMidi, position, warmUpVoice, voice, midiTrack);
        }
        return voiceEndPosition;
    }

    private long addStepVoice(Set<MidiEventShort> events, int currentTonicMidi, long position,
                              WarmUpVoice warmUpVoice, Accompaniment.Voice voice, MidiTrack midiTrack) {

        int octaveShift = getOctaveShift(voice.getOctaveShifts(), currentTonicMidi);
//        System.out.println("Octave shift " + octaveShift + " for " + midiTrack);

        for (MusicalSymbol symbol : warmUpVoice.getMusicalSymbols()) {
//            System.out.println("!!! " + symbol);
            long duration = MidiUtils.getNoteValueInTicks(symbol.getNoteValue());
            if (symbol.isSounding()) {
                int note = MidiUtils.transpose(((Note) symbol).getNoteRegister(), currentTonicMidi, octaveShift);
//                    System.out.println("Note " + note + " at position " + position + " " + channel + " " + duration);
                MidiEventShort midiEventStart = new MidiEventShort(ShortMessage.NOTE_ON, voice.getInstrument(), note, position);
                events.add(midiEventStart);

                MidiEventShort midiEventEnd = new MidiEventShort(ShortMessage.NOTE_OFF, voice.getInstrument(), note, position + duration);
                events.add(midiEventEnd);
            }

            position += duration;
        }
        return position;
    }

    private static int getOctaveShift(OctaveShifts shifts, int tonicMidi) {
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

    // TODO: Probably this is not needed
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