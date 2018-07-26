package com.berniac.vocalwarmup.sequence.adjustment;

import com.berniac.vocalwarmup.music.MusicalSymbol;
import com.berniac.vocalwarmup.music.NoteSymbol;
import com.berniac.vocalwarmup.music.NoteValue;
import com.berniac.vocalwarmup.music.Rest;
import com.berniac.vocalwarmup.sequence.WarmUpVoice;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mikhail Lipkovich on 1/19/2018.
 */
public class SilentAdjustmentRulesTest {/*
    @Test
    public void testQuarterPause() {
        SilentAdjustmentRules adjustmentRules = SilentAdjustmentRules.valueOf(null);
        Map<NoteSymbol, List<WarmUpVoice>> actual = adjustmentRules.getAdjustmentRules(1);
        Map<NoteSymbol, List<WarmUpVoice>> expected = new HashMap<>();
        for (NoteSymbol noteSymbol : NoteSymbol.values()) {
            expected.put(noteSymbol, Collections.singletonList(
                    new WarmUpVoice(
                            Collections.singletonList((MusicalSymbol) new Rest(NoteValue.QUARTER)),
                            null))
            );
        }

        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testWholePause() {
        SilentAdjustmentRules adjustmentRules = SilentAdjustmentRules.valueOf(null);
        Map<NoteSymbol, List<WarmUpVoice>> actual = adjustmentRules.getAdjustmentRules(4);
        Map<NoteSymbol, List<WarmUpVoice>> expected = new HashMap<>();
        for (NoteSymbol noteSymbol : NoteSymbol.values()) {
            expected.put(noteSymbol, Collections.singletonList(
                    new WarmUpVoice(
                            Arrays.asList((MusicalSymbol) new Rest(NoteValue.QUARTER),
                                    (MusicalSymbol) new Rest(NoteValue.QUARTER),
                                    (MusicalSymbol) new Rest(NoteValue.QUARTER),
                                    (MusicalSymbol) new Rest(NoteValue.QUARTER)),
                            null))
            );
        }

        Assert.assertEquals(expected, actual);
    }*/
}
