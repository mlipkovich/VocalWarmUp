package com.berniac.vocalwarmup.sequence;

import com.berniac.vocalwarmup.music.MusicalSymbol;

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

    public WarmUpVoice valueOf(String s) {
        // TODO: Marina implement using MusicalSymbolParser
        return null;
    }

    public MusicalSymbol[] getMusicalSymbols() {
        return musicalSymbols;
    }

    public Instrument getInstrument() {
        return instrument;
    }
}
