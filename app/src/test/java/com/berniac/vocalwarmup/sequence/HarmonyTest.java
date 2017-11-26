package com.berniac.vocalwarmup.sequence;

import com.berniac.vocalwarmup.music.MusicalSymbol;
import com.berniac.vocalwarmup.music.Note;
import com.berniac.vocalwarmup.music.NoteSymbol;
import com.berniac.vocalwarmup.music.NoteValue;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by Mikhail Lipkovich on 11/17/2017.
 */
public class HarmonyTest {
    @Test
    public void testParsing() throws Exception {
        String nl = System.lineSeparator();
        MusicalSymbol[] musicalSymbols = new MusicalSymbol[]{
                new Note(NoteSymbol.C, 0, NoteValue.QUARTER),
                new Note(NoteSymbol.C_SHARP, 0, NoteValue.QUARTER),
                new Note(NoteSymbol.D, 0, NoteValue.QUARTER)
        };
        WarmUpVoice voiceFo = new WarmUpVoice(musicalSymbols, Instrument.FORTEPIANO);
        WarmUpVoice voiceAc = new WarmUpVoice(musicalSymbols, Instrument.ACORDEON);
        WarmUpVoice voiceMe = new WarmUpVoice(musicalSymbols, Instrument.MELODIC_VOICE);

        Harmony harmony = new Harmony(Arrays.asList(voiceFo, voiceAc, voiceMe));
        Assert.assertEquals(harmony,
                Harmony.valueOf("Fo(4C,4Cis,4D)" + nl + "Ac(4C,4Cis,4D)" + nl + "Me(4C,4Cis,4D)"));
    }
}
