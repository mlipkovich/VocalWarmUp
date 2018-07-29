package com.berniac.vocalwarmup.music;

/**
 * Created by Mikhail Lipkovich on 11/17/2017.
 */
public class MusicalSymbolParser {
    private static final String DEFAULT_OCTAVE = "0";

    public static MusicalSymbol parse(String str) {
        MusicalSymbol currentSymbol;

        int noteValueEnd = -1;
        int symbolEnd = str.length();
        int octaveEnd = str.length();

        for (int i = 0; i < str.length(); i++) {
            if ((noteValueEnd == -1) & Character.isLetter(str.charAt(i))) {
                noteValueEnd = i;
            } else if ((noteValueEnd != -1) & (symbolEnd == str.length())
                    & !Character.isLetter(str.charAt(i))) {
                symbolEnd = i;
            }
        }

        String noteValue = str.substring(0, noteValueEnd);
        String symbol = str.substring(noteValueEnd, symbolEnd);
        String octave = str.substring(symbolEnd, octaveEnd);

        if (octave.equals("")) {
            octave = DEFAULT_OCTAVE;
        }

        if (symbol.equals(Rest.REST_SYMBOL)) {
            if (symbolEnd != octaveEnd) {
                throw new IllegalArgumentException("Rest shouldn't contain octave. Your string to valueOf: " +
                        octave);
            }
            currentSymbol = new Rest(NoteValue.getByCode(noteValue));
        } else {
            currentSymbol = new Note(NoteSymbol.getByCode(symbol), Integer.valueOf(octave),
                                     NoteValue.getByCode(noteValue));
        }
        return currentSymbol;
    }
}
