package com.berniac.vocalwarmup.sequence.adjustment;

import com.berniac.vocalwarmup.music.NoteSymbol;
import com.berniac.vocalwarmup.sequence.Playable;
import com.berniac.vocalwarmup.sequence.WarmUpVoice;

import java.util.List;
import java.util.Map;

/**
 * Created by Mikhail Lipkovich on 12/24/2017.
 */
public class Adjustment {

    private Map<NoteSymbol, List<WarmUpVoice>> tonicToVoices;

    public Adjustment(AdjustmentRules adjustmentRules, int pauseQuartersCount) {
        this.tonicToVoices = adjustmentRules.getAdjustmentRules(pauseQuartersCount);
    }

    public Playable getVoices(NoteSymbol fromNote, NoteSymbol toNote) {
        final NoteSymbol tonic = getNewTonicInC(fromNote, toNote);
        return new Playable() {
            @Override
            public List<WarmUpVoice> getVoices() {
                return tonicToVoices.get(tonic);
            }

            @Override
            public String toString() {
                return getVoices().toString();
            }
        };
    }

    private NoteSymbol getNewTonicInC(NoteSymbol fromNote, NoteSymbol toNote) {
        int distFromCFromNote = getNumberOfSemitonesFromC(fromNote);
        int distFromCToNote = getNumberOfSemitonesFromC(toNote);
        int distBetweenFromTo = distFromCToNote - distFromCFromNote;
        return getNoteInDistanceFromC(distBetweenFromTo);
    }

    private static NoteSymbol getNoteInDistanceFromC(int numberOfSemitones) {
        // TODO: Make sure which note symbols to use. think about bidirectional map
        numberOfSemitones = (numberOfSemitones + 12) % 12;
        if (numberOfSemitones == 0) {
            return NoteSymbol.C;
        } else if (numberOfSemitones == 1) {
            return NoteSymbol.D_BEMOL;
        } else if (numberOfSemitones == 2) {
            return NoteSymbol.D;
        } else if (numberOfSemitones == 3) {
            return NoteSymbol.E_BEMOL;
        } else if (numberOfSemitones == 4) {
            return NoteSymbol.E;
        } else if (numberOfSemitones == 5) {
            return NoteSymbol.F;
        } else if (numberOfSemitones == 6) {
            return NoteSymbol.F_SHARP;
        } else if (numberOfSemitones == 7) {
            return NoteSymbol.G;
        } else if (numberOfSemitones == 8) {
            return NoteSymbol.A_BEMOL;
        } else if (numberOfSemitones == 9) {
            return NoteSymbol.A;
        } else if (numberOfSemitones == 10) {
            return NoteSymbol.H_BEMOL;
        } else { // numberOfSemitones == 11
            return NoteSymbol.H;
        }
    }

    private static int getNumberOfSemitonesFromC(NoteSymbol noteSymbol) {
        switch (noteSymbol) {
            case C:
            case H_SHARP:
                return 0;
            case C_SHARP:
            case D_BEMOL:
                return 1;
            case D:
                return 2;
            case D_SHARP:
            case E_BEMOL:
                return 3;
            case E:
            case F_BEMOL:
                return 4;
            case F:
            case E_SHARP:
                return 5;
            case F_SHARP:
            case G_BEMOL:
                return 6;
            case G:
                return 7;
            case G_SHARP:
            case A_BEMOL:
                return 8;
            case A:
                return 9;
            case A_SHARP:
            case H_BEMOL:
                return 10;
            case H:
            case C_BEMOL:
                return 11;
            default:
                throw new IllegalArgumentException("Unknown note " + noteSymbol);
        }
    }
}
