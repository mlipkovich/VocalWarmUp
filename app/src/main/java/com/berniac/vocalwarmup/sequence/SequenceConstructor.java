package com.berniac.vocalwarmup.sequence;

import com.berniac.vocalwarmup.midi.MidiUtils;
import com.berniac.vocalwarmup.music.MusicalSymbol;
import com.berniac.vocalwarmup.music.Note;
import com.berniac.vocalwarmup.music.NoteRegister;
import com.berniac.vocalwarmup.music.NoteValue;

import jp.kshoji.javax.sound.midi.InvalidMidiDataException;
import jp.kshoji.javax.sound.midi.MidiEvent;
import jp.kshoji.javax.sound.midi.MidiMessage;
import jp.kshoji.javax.sound.midi.Sequence;
import jp.kshoji.javax.sound.midi.ShortMessage;
import jp.kshoji.javax.sound.midi.Track;

/**
 * Created by Mikhail Lipkovich on 11/10/2017.
 */
public class SequenceConstructor {

    private static final int TICKS_IN_QUARTER_NOTE =
            MidiUtils.getNoteValueInTicks(NoteValue.QUARTER);

    // TODO: Crescendo/diminuendo
    private static final int VOLUME = 100;

    private SequenceConstructor(){}


    public static Sequence construct(WarmUp warmUp) throws Exception {

        Sequence sequence = new Sequence(Sequence.PPQ, TICKS_IN_QUARTER_NOTE, MidiTrack.values().length);
        int step = 2; // TODO: get step from WarmUp.Step. Deal somehow with Random
        if (warmUp.getMelody() != null) {
            Track melody = sequence.getTracks()[MidiTrack.MELODY.index];
            fillTrack(melody, warmUp.getMelody(), warmUp.getStartingNote(), warmUp.getLowerNote(),
                    warmUp.getUpperNote(), step, warmUp.getDirections());
        }

        if (warmUp.getHarmony() != null) {
            Track harmony = sequence.getTracks()[MidiTrack.HARMONY.index];
            fillTrack(harmony, warmUp.getHarmony(), warmUp.getStartingNote(), warmUp.getLowerNote(),
                    warmUp.getUpperNote(), step, warmUp.getDirections());
        }

        // TODO: Add metronome and lyrics

        return sequence;
    }

    private static void fillTrack(Track track, Playable playable,
                                  NoteRegister startNote, NoteRegister lowerNote, NoteRegister upperNote,
                                  int step, Direction[] directions) throws InvalidMidiDataException {
        long previousTick = 0;
        for (WarmUpVoice voice : playable.getVoices()) {
            NoteRegister startingNote;
            int orientedStep;
            for (Direction direction : directions) {
                switch (direction) {
                    case START_TO_LOWER:
                        startingNote = startNote;
                        orientedStep = -step;
                        break;
                    case START_TO_UPPER:
                        startingNote = startNote;
                        orientedStep = step;
                        break;
                    case LOWER_TO_START:
                        startingNote = lowerNote;
                        orientedStep = step;
                        break;
                    case UPPER_TO_START:
                        startingNote = upperNote;
                        orientedStep = -step;
                        break;
                    default:
                        throw new IllegalStateException("There is no value " + direction + " for directions");
                }
                previousTick = addDirection(track, lowerNote, upperNote, startingNote,
                        orientedStep, voice, previousTick);
            }
        }
    }

    private static long addDirection(Track track, NoteRegister lowerNote, NoteRegister upperNote,
                                     NoteRegister startingNote, int orientedStep,
                                     WarmUpVoice voice, long previousTick) throws InvalidMidiDataException {

        int lowerNoteMidi = MidiUtils.getMidiNote(lowerNote);
        int upperNoteMidi = MidiUtils.getMidiNote(upperNote);
        int note = MidiUtils.getMidiNote(startingNote);
        System.out.println("Adding direction " + lowerNoteMidi + " " + upperNoteMidi + " " + note);
        while (note >= lowerNoteMidi && note <= upperNoteMidi) {
            System.out.println("Note " + note + " " + previousTick);
            previousTick = addStep(track, voice, note, previousTick);
            note += orientedStep;
        }
        return previousTick;
    }

    private static long addStep(Track track, WarmUpVoice voice, int tonic, long previousTick)
            throws InvalidMidiDataException {

        // TODO: Define channel by voice.getInstrument()
        int channel = 0;

        for (MusicalSymbol symbol : voice.getMusicalSymbols()) {
            long duration = MidiUtils.getNoteValueInTicks(symbol.getNoteValue());
            if (symbol.isSounding()) {
                int midiNote = MidiUtils.transpose(((Note) symbol).getNoteRegister(), tonic);
                addNote(track, channel, midiNote, duration, previousTick);
            }
            previousTick += duration;
        }
        return previousTick;
    }

    private static void addNote(Track track, int channel, int note, long duration, long position) throws InvalidMidiDataException {
        MidiMessage midiMessage = new ShortMessage(ShortMessage.NOTE_ON, channel, note, VOLUME);
        MidiEvent midiEvent = new MidiEvent(midiMessage, position);
        track.add(midiEvent);
        midiMessage = new ShortMessage(ShortMessage.NOTE_OFF, channel, note, VOLUME);
        midiEvent = new MidiEvent(midiMessage, position + duration);
        track.add(midiEvent);
    }

    enum MidiTrack {
        MELODY(0),
        METRONOME(1),
        HARMONY(2),
        LYRICS(3);

        private int index;
        MidiTrack(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }
}
