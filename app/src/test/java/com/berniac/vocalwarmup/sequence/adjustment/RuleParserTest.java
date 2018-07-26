package com.berniac.vocalwarmup.sequence.adjustment;

import com.berniac.vocalwarmup.music.MusicalSymbol;
import com.berniac.vocalwarmup.music.Note;
import com.berniac.vocalwarmup.music.NoteSymbol;
import com.berniac.vocalwarmup.music.NoteValue;
import com.berniac.vocalwarmup.music.Rest;
import com.berniac.vocalwarmup.sequence.Instrument;
import com.berniac.vocalwarmup.sequence.WarmUpVoice;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mikhail Lipkovich on 1/16/2018.
 */
public class RuleParserTest {
/*
    private List<WarmUpVoice> drums;
    private Map<NoteSymbol, List<WarmUpVoice>> harmony;

    @Before
    public void setUp() {
        drums = new ArrayList<>();
        drums.add(new WarmUpVoice(Arrays.asList(new MusicalSymbol[]{
                new Note(NoteSymbol.C, -2, NoteValue.HALF),
                new Note(NoteSymbol.D, -2, NoteValue.HALF)}), Instrument.DRUMS));
        drums.add(new WarmUpVoice(Arrays.asList(new MusicalSymbol[]{
                new Note(NoteSymbol.E, -2, NoteValue.QUARTER),
                new Note(NoteSymbol.E, -2, NoteValue.QUARTER),
                new Note(NoteSymbol.E, -2, NoteValue.QUARTER),
                new Note(NoteSymbol.F, -2, NoteValue.QUARTER)}), Instrument.DRUMS));


        List<WarmUpVoice> voices = new ArrayList<>();
        voices.add(new WarmUpVoice(Arrays.asList(
                new Note(NoteSymbol.C, 0, NoteValue.EIGHTH),
                new Rest(NoteValue.EIGHTH),
                new Note(NoteSymbol.C, 0, NoteValue.QUARTER)), Instrument.CLEAN_GUITAR));
        voices.add(new WarmUpVoice(Arrays.asList(
                new Note(NoteSymbol.E, 0, NoteValue.QUARTER),
                new Rest(NoteValue.EIGHTH),
                new Note(NoteSymbol.E, 0, NoteValue.EIGHTH)), Instrument.CLEAN_GUITAR));
        voices.add(new WarmUpVoice(Arrays.asList(
                new Note(NoteSymbol.G, -1, NoteValue.EIGHTH),
                new Rest(NoteValue.EIGHTH),
                new Note(NoteSymbol.C, -1, NoteValue.QUARTER)), Instrument.BASS_GUITAR));
        harmony = new HashMap<>();
        harmony.put(NoteSymbol.C, voices);
    }

    @Test
    public void parseDrums() {
        String rule = "Dr[Dr(2C-2,2D-2) Dr(4E-2,4E-2,4E-2,4F-2)]";
        RuleParser ruleParser = RuleParser.valueOf(rule);

        Assert.assertEquals(Collections.emptyMap(), ruleParser.getHarmony());
        Assert.assertEquals(drums, ruleParser.getDrums());
    }

    @Test
    public void parseHarmony() {
        String rule = "C[Gu(8C,8N,4C) Gu(4E,8N,8E) Ba(8G-1,8N,4C-1)]";
        RuleParser ruleParser = RuleParser.valueOf(rule);

        Assert.assertEquals(harmony, ruleParser.getHarmony());
        Assert.assertEquals(Collections.emptyList(), ruleParser.getDrums());
    }

    @Test
    public void parseTogether() {
        String rule = "Dr[Dr(2C-2,2D-2) Dr(4E-2,4E-2,4E-2,4F-2)]" + "\n" +
                "C[Gu(8C,8N,4C) Gu(4E,8N,8E) Ba(8G-1,8N,4C-1)]";
        RuleParser ruleParser = RuleParser.valueOf(rule);

        Assert.assertEquals(harmony, ruleParser.getHarmony());
        Assert.assertEquals(drums, ruleParser.getDrums());
    }*/
}