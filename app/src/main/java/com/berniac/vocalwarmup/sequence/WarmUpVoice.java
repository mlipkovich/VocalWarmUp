package com.berniac.vocalwarmup.sequence;

import com.berniac.vocalwarmup.music.MusicalSymbol;
import com.berniac.vocalwarmup.music.MusicalSymbolParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marina Gorlova on 13.11.2017.
 */
public class WarmUpVoice {

    private List<MusicalSymbol> musicalSymbols;
    private Instrument instrument;

    public WarmUpVoice(List<MusicalSymbol> musicalSymbols, Instrument instrument) {
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
        List<MusicalSymbol> musicalSymbols = new ArrayList<MusicalSymbol>(notes.length);
        for (String note : notes) {
            musicalSymbols.add(MusicalSymbolParser.parse(note));
        }

        return new WarmUpVoice(musicalSymbols, instrument);
    }

    public List<MusicalSymbol> getMusicalSymbols() {
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

        return musicalSymbols.equals(that.musicalSymbols) && instrument == that.instrument;
    }

    @Override
    public String toString() {
        return "WarmUpVoice{" +
                "musicalSymbols=" + musicalSymbols +
                ", instrument=" + instrument +
                '}';
    }
}
