package com.berniac.vocalwarmup.sequence;

import com.berniac.vocalwarmup.music.MusicalSymbol;
import com.berniac.vocalwarmup.music.Note;
import com.berniac.vocalwarmup.music.NoteSymbol;
import com.berniac.vocalwarmup.music.NoteValue;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Mikhail Lipkovich on 11/17/2017.
 */
public class HarmonyTest {
    @Test
    public void testParsing() throws Exception {
        String nl = System.lineSeparator();
        List<MusicalSymbol> musicalSymbols = Arrays.asList(new MusicalSymbol[]{
                new Note(NoteSymbol.C, 0, NoteValue.QUARTER),
                new Note(NoteSymbol.C_SHARP, 0, NoteValue.QUARTER),
                new Note(NoteSymbol.D, 0, NoteValue.QUARTER)
        });
        WarmUpVoice voiceFo = new WarmUpVoice(musicalSymbols, Instrument.FORTEPIANO);
        WarmUpVoice voiceAc = new WarmUpVoice(musicalSymbols, Instrument.ACORDEON);
        WarmUpVoice voiceMe = new WarmUpVoice(musicalSymbols, Instrument.MELODIC_VOICE);

        Harmony harmony = new Harmony(Arrays.asList(voiceFo, voiceAc, voiceMe));
        Assert.assertEquals(harmony,
                Harmony.valueOf("Fo(4C,4Cis,4D)" + nl + "Ac(4C,4Cis,4D)" + nl + "Me(4C,4Cis,4D)"));
    }

    @Test
    public void testParsingWithShifts() throws Exception {
        Harmony harmony = Harmony.valueOf("Fo(2E1,4E1,4G1,2F1,8G1,8F1,8E1,8D1,2E1){D-1,+1<Cis1,-1;D2,-2}" + "\n" +
                "Fo(25C1,4A,1H,2C1){D-1,+1<Hes,-1;Hes1,-2;E2,-3}" + "\n" +
                "Fo(25G,4F,25G,4A,2G){F-1,+1<Aes,-1;Aes1,-2;E2,-3}" + "\n" +
                "Fo(2C-1,2G-1,2D-1,2G-2,2C-1){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}");

        System.out.println(harmony.getVoices().get(2));
    }
}
