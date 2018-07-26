package com.berniac.vocalwarmup.sequence;

import com.berniac.vocalwarmup.music.MusicalSymbol;
import com.berniac.vocalwarmup.music.Note;
import com.berniac.vocalwarmup.music.NoteSymbol;
import com.berniac.vocalwarmup.music.NoteValue;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marina Gorlova on 24.11.2017.
 */
public class WarmUpVoiceTest {/*
    @Test
    public void testParsingVoice() throws Exception {
        List<MusicalSymbol> musicalSymbols = new ArrayList<>();
        musicalSymbols.add(new Note(NoteSymbol.C, 0, NoteValue.QUARTER));
        musicalSymbols.add(new Note(NoteSymbol.C_SHARP, 0, NoteValue.QUARTER));
        musicalSymbols.add(new Note(NoteSymbol.D, 0, NoteValue.QUARTER));

        WarmUpVoice warmUpVoice = new WarmUpVoice(musicalSymbols, Instrument.FORTEPIANO);
        Assert.assertEquals(warmUpVoice, WarmUpVoice.valueOf("Fo(4C,4Cis,4D)"));

        musicalSymbols = new ArrayList<>();
        musicalSymbols.add(new Note(NoteSymbol.G, 0, NoteValue.QUARTER));
        musicalSymbols.add(new Note(NoteSymbol.C_SHARP, 0, NoteValue.SIXTEETH));
        musicalSymbols.add(new Note(NoteSymbol.D, 3, NoteValue.QUARTER));
        warmUpVoice = new WarmUpVoice(musicalSymbols, Instrument.ELECTROPIANO);
        Assert.assertEquals(warmUpVoice, WarmUpVoice.valueOf("El(4G,16Cis,4D3)"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParsingSkipLeftBracket() throws Exception {
        WarmUpVoice.valueOf("El4G,16Cis,4D3)");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParsingSkipRightBracket() throws Exception {
        WarmUpVoice.valueOf("El(4G,16Cis,4D3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParsingSkipInstrument() throws Exception {
        WarmUpVoice.valueOf("(4G,16Cis,4D3)");
    }*/
}
