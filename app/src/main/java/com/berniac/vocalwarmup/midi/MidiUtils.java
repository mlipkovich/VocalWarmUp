package com.berniac.vocalwarmup.midi;

import com.berniac.vocalwarmup.music.NoteRegister;
import com.berniac.vocalwarmup.music.NoteSymbol;
import com.berniac.vocalwarmup.music.NoteValue;

/**
 * Created by Mikhail Lipkovich on 11/17/2017.
 * Useful utils for conversion between musical notations and midi notes
 */
public class MidiUtils {

    private static final int WHOLE_NOTE_VALUE_TICKS = 384;
    private static final int SMALL_OCTAVE_C_MIDI = 48;
    private static final int SEMITONES_IN_OCTAVE = 12;

    /**
     * Takes note in C-major scale and transposes it with respect to tonic which is given
     * as midi value. Result is also a midi value
     */
    public static int transpose(NoteRegister noteInCMajorScale, int newTonicMidi, int octaveShift) {
        int noteInCMajorScaleMidi = getMidiNote(noteInCMajorScale);
        int shiftFromCMajorTonic = noteInCMajorScaleMidi - SMALL_OCTAVE_C_MIDI;
        int transposedMidi = shiftFromCMajorTonic + newTonicMidi;
        int shiftedTransposedMidi = transposedMidi + octaveShift * SEMITONES_IN_OCTAVE;
        validateMidiValue(shiftedTransposedMidi);
        return shiftedTransposedMidi;
    }

    public static NoteSymbol getNote(int midiValue) {
        int numberOfSemitones = midiValue % 12;
        if (numberOfSemitones == 0) {
            return NoteSymbol.C;
        } else if (numberOfSemitones == 1) {
            return NoteSymbol.C_SHARP;
        } else if (numberOfSemitones == 2) {
            return NoteSymbol.D;
        } else if (numberOfSemitones == 3) {
            return NoteSymbol.D_SHARP;
        } else if (numberOfSemitones == 4) {
            return NoteSymbol.E;
        } else if (numberOfSemitones == 5) {
            return NoteSymbol.F;
        } else if (numberOfSemitones == 6) {
            return NoteSymbol.F_SHARP;
        } else if (numberOfSemitones == 7) {
            return NoteSymbol.G;
        } else if (numberOfSemitones == 8) {
            return NoteSymbol.G_SHARP;
        } else if (numberOfSemitones == 9) {
            return NoteSymbol.A;
        } else if (numberOfSemitones == 10) {
            return NoteSymbol.A_SHARP;
        } else { // numberOfSemitones == 11
            return NoteSymbol.H;
        }
    }

    public static int getMidiNote(NoteRegister note) {
        int semitonesToC;
        switch (note.getNoteSymbol()) {
            case C_BEMOL:
                semitonesToC = -1;
                break;
            case C:
                semitonesToC = 0;
                break;
            case C_SHARP:
            case D_BEMOL:
                semitonesToC = 1;
                break;
            case D:
                semitonesToC = 2;
                break;
            case D_SHARP:
            case E_BEMOL:
                semitonesToC = 3;
                break;
            case E:
            case F_BEMOL:
                semitonesToC = 4;
                break;
            case E_SHARP:
            case F:
                semitonesToC = 5;
                break;
            case F_SHARP:
            case G_BEMOL:
                semitonesToC = 6;
                break;
            case G:
                semitonesToC = 7;
                break;
            case G_SHARP:
            case A_BEMOL:
                semitonesToC = 8;
                break;
            case A:
                semitonesToC = 9;
                break;
            case A_SHARP:
            case H_BEMOL:
                semitonesToC = 10;
                break;
            case H:
                semitonesToC = 11;
                break;
            case H_SHARP:
                semitonesToC = 12;
                break;
            default:
                throw new IllegalArgumentException("There is no such note " + note.getNoteSymbol());
        }

        int currentOctaveCMidi = note.getOctave()*SEMITONES_IN_OCTAVE + SMALL_OCTAVE_C_MIDI;
        validateMidiValue(currentOctaveCMidi);
        int noteMidi = currentOctaveCMidi + semitonesToC;
        validateMidiValue(noteMidi);
        return noteMidi;
    }

    public static int getNoteValueInTicks(NoteValue noteValue) {
        return noteValue.getNumerator() * WHOLE_NOTE_VALUE_TICKS / noteValue.getDenominator();
    }

    public static int semitonesBetween(NoteRegister from, NoteRegister to) {
        return getMidiNote(to) - getMidiNote(from);
    }

    public static void validateMidiValue(int midi) {
        if (midi < 0 | midi > 255) {
            throw new IllegalStateException("Midi value " + midi + " should be between 0 and 255");
        }
    }

    public  static int getTonic() {
        return SMALL_OCTAVE_C_MIDI;
    }
}
