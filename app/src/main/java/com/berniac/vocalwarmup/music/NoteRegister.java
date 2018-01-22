package com.berniac.vocalwarmup.music;

import static com.berniac.vocalwarmup.midi.MidiUtils.getMidiNote;

/**
 * Created by Mikhail Lipkovich on 11/17/2017.
 */
public class NoteRegister {

    // TODO: Ask Ilya about exact boundaries
    public static final NoteRegister LOWEST_NOTE = new NoteRegister(NoteSymbol.C, -3);
    public static final NoteRegister HIGHEST_NOTE = new NoteRegister(NoteSymbol.C, 5);

    private static final int LOWER_OCTAVE = -3;
    private static final int UPPER_OCTAVE = 5;

    private NoteSymbol noteSymbol;
    private int octave;

    public NoteRegister(NoteSymbol noteSymbol, int octave) {
        if (octave < LOWER_OCTAVE || octave > UPPER_OCTAVE) {
            throw new IllegalArgumentException("Octave value " + octave +
                    " should be between " + LOWER_OCTAVE + " and " + UPPER_OCTAVE);
        }
        if (octave == LOWER_OCTAVE && noteSymbol == NoteSymbol.C_BEMOL) {
            throw new IllegalArgumentException("Note " + noteSymbol +
                    " is out of boundaries for octave " + octave);
        }
        if (octave == UPPER_OCTAVE && noteSymbol == NoteSymbol.H_SHARP) {
            throw new IllegalArgumentException("Note " + noteSymbol +
                    " is out of boundaries for octave " + octave);
        }

        this.noteSymbol = noteSymbol;
        this.octave = octave;
    }

    public NoteSymbol getNoteSymbol() {
        return noteSymbol;
    }

    public int getOctave() {
        return octave;
    }

    public static int semitonesBetween(NoteRegister from, NoteRegister to) {
        return getMidiNote(to) - getMidiNote(from);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoteRegister)) return false;

        NoteRegister that = (NoteRegister) o;

        if (octave != that.octave) return false;
        return noteSymbol == that.noteSymbol;
    }
}
