package com.berniac.vocalwarmup.midi;

import com.berniac.vocalwarmup.music.NoteRegister;
import com.berniac.vocalwarmup.music.NoteValue;

/**
 * Created by Mikhail Lipkovich on 11/17/2017.
 * Useful utils for conversion between musical notations and midi notes
 */
public class MidiUtils {

    private static final int SMALL_OCTAVE_C_MIDI = 36;
    private static final int SEMITONES_IN_OCTAVE = 12;

    /**
     * Takes note in C-major scale and transposes it with respect to tonic which is given
     * as midi value. Result is also a midi value
     */
    public static int transpose(NoteRegister noteInCMajorScale, int newTonicMidi) {
        int noteInCMajorScaleMidi = getMidiNote(noteInCMajorScale);
        int shiftFromCMajorTonic = noteInCMajorScaleMidi - SMALL_OCTAVE_C_MIDI;
        int transposedMidi = shiftFromCMajorTonic + newTonicMidi;
        validateMidiValue(transposedMidi);
        return transposedMidi;
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
        switch (noteValue) {
            case WHOLE:
                return 384;
            case HALF:
                return 192;
            case HALF_DOTTED:
                return 288;
            case QUARTER:
                return 96;
            case QUARTER_DOTTED:
                return 144;
            case QUARTER_TRIOLE:
                return 64;
            case EIGHTH:
                return 48;
            case EIGHTH_DOTTED:
                return 72;
            case EIGHTH_TRIOLE:
                return 32;
            case SIXTEETH:
                return 24;
            case SIXTEETH_DOTTED:
                return 36;
            case SIXTEETH_TRIOLE:
                return 16;
            case THIRTYSECOND:
                return 12;
            case THIRTYSECOND_DOTTED:
                return 18;
            case THIRTYSECOND_TRIOLE:
                return 8;
            case SIXTYFOURTH:
                return 6;
            default:
                throw new IllegalArgumentException("There is no such note value " + noteValue);
        }
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
