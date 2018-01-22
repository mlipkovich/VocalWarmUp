package com.berniac.vocalwarmup.sequence;

import com.berniac.vocalwarmup.midi.MidiUtils;
import com.berniac.vocalwarmup.music.MusicalSymbol;
import com.berniac.vocalwarmup.music.MusicalSymbolParser;

import java.util.Arrays;

/**
 * Created by Marina Gorlova on 13.11.2017.
 */
public class WarmUpVoice {

    private MusicalSymbol[] musicalSymbols;
    private Instrument instrument;

    public WarmUpVoice(MusicalSymbol[] musicalSymbols, Instrument instrument) {
        this.musicalSymbols = musicalSymbols;
        this.instrument = instrument;
    }

    public static WarmUpVoice valueOf(String str) {
        int voiceStart = str.indexOf('(');
        int voiceEnd = str.indexOf(')');

        if (voiceStart == -1 || voiceEnd == -1 || voiceStart > voiceEnd) {
            throw new IllegalArgumentException("Voice " + str +
                    " should start with '(' and end with ')'." );
        }

        if (voiceStart == 0) {
            throw new IllegalArgumentException("Voice " + str +
                    " should start with an instrument.");
        }

        Instrument instrument = Instrument.getByCode(str.substring(0, voiceStart));

        String[] notes = str.substring(voiceStart + 1, voiceEnd).split(",");
        MusicalSymbol[] musicalSymbols = new MusicalSymbol[notes.length];
        int i = 0;
        for (String note : notes) {
            musicalSymbols[i] = MusicalSymbolParser.parse(note);
            i++;
        }

        return new WarmUpVoice(musicalSymbols, instrument);
    }

    public int getLength() {
        int length = 0;
        for (MusicalSymbol symbol: musicalSymbols) {
            length += MidiUtils.getNoteValueInTicks(symbol.getNoteValue());
        }
        return length;
    }

    public MusicalSymbol[] getMusicalSymbols() {
        return musicalSymbols;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WarmUpVoice)) return false;

        WarmUpVoice that = (WarmUpVoice) o;

        if (!Arrays.equals(musicalSymbols, that.musicalSymbols)) return false;
        return instrument == that.instrument;
    }
}
