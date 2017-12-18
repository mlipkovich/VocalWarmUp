package com.berniac.vocalwarmup.sequence;

import com.berniac.vocalwarmup.midi.MidiUtils;
import com.berniac.vocalwarmup.music.MusicalSymbol;
import com.berniac.vocalwarmup.music.Note;
import com.berniac.vocalwarmup.music.NoteRegister;
import com.berniac.vocalwarmup.music.NoteValue;

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
            fillTrack(melody, MidiTrack.MELODY, warmUp.getMelody(), step);
        }

        if (warmUp.getHarmony() != null) {
            Track harmony = sequence.getTracks()[MidiTrack.HARMONY.index];
            fillTrack(harmony, MidiTrack.HARMONY, warmUp.getHarmony(), step);
        }

        // TODO: Add metronome and lyrics

        return sequence;
    }

    private static void fillTrack(Track track, MidiTrack midiTrack, Playable playable,
                                  int step) throws InvalidMidiDataException {
        long previousTick = 0;
        for (WarmUpVoice voice : playable.getVoices()) {
            NoteRegister lowestNote = NoteRegister.LOWEST_NOTE;
            NoteRegister highestNote = NoteRegister.HIGHEST_NOTE;

            int orientedStep = step;
            previousTick = addDirection(track, midiTrack, voice, lowestNote, highestNote,
                    orientedStep, previousTick);
            orientedStep = -step;
            previousTick = addDirection(track, midiTrack, voice, highestNote, lowestNote,
                    orientedStep, previousTick);
        }
    }

    private static long addDirection(Track track, MidiTrack midiTrack, WarmUpVoice voice,
                                     NoteRegister startNote, NoteRegister endNote,
                                     int orientedStep, long previousTick) throws InvalidMidiDataException {

        int startNoteMidi = MidiUtils.getMidiNote(startNote);
        int endNoteMidi = MidiUtils.getMidiNote(endNote);
        int note = startNoteMidi;
        System.out.println("Adding direction from " + startNoteMidi + " to " + endNoteMidi);
        while (note >= startNoteMidi && note <= endNoteMidi) {
            System.out.println("Adding step from " + note + " with tick " + previousTick);
            previousTick = addStep(track, midiTrack, voice, note, previousTick);
            note += orientedStep;
        }
        return previousTick;
    }

    private static long addStep(Track track, MidiTrack midiTrack, WarmUpVoice voice,
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

    private static void addNote(Track track, MidiTrack midiTrack, int channel, int note,
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
