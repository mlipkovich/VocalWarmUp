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
    private OctaveShifts octaveShifts;

    public WarmUpVoice(List<MusicalSymbol> musicalSymbols, Instrument instrument,
                       OctaveShifts octaveShifts) {
        this.musicalSymbols = musicalSymbols;
        this.instrument = instrument;
        this.octaveShifts = octaveShifts;
    }

    public WarmUpVoice(List<MusicalSymbol> musicalSymbols, Instrument instrument) {
        this.musicalSymbols = musicalSymbols;
        this.instrument = instrument;
        this.octaveShifts = OctaveShifts.EMPTY_SHIFTS;
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

        OctaveShifts shifts = OctaveShifts.valueOf(str.substring(voiceEnd + 1, str.length()));
        return new WarmUpVoice(musicalSymbols, instrument, shifts);
    }

    public List<MusicalSymbol> getMusicalSymbols() {
        return musicalSymbols;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public OctaveShifts getOctaveShifts() {
        return octaveShifts;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WarmUpVoice that = (WarmUpVoice) o;

        if (!musicalSymbols.equals(that.musicalSymbols)) return false;
        if (instrument != that.instrument) return false;
        return octaveShifts.equals(that.octaveShifts);

    }

    @Override
    public int hashCode() {
        int result = musicalSymbols.hashCode();
        result = 31 * result + instrument.hashCode();
        result = 31 * result + octaveShifts.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "WarmUpVoice{" +
                "musicalSymbols=" + musicalSymbols +
                ", instrument=" + instrument +
                ", octaveShifts=" + octaveShifts +
                '}';
    }
}
