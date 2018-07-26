package com.berniac.vocalwarmup.sequence.adjustment;

import com.berniac.vocalwarmup.music.NoteSymbol;
import com.berniac.vocalwarmup.sequence.Harmony;
import com.berniac.vocalwarmup.sequence.WarmUpVoice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mikhail Lipkovich on 12/23/2017.
 */
public class RuleParser {

    private static final String DRUM_RULES = "Dr";

    private List<WarmUpVoice> drums;
    private Map<NoteSymbol, List<WarmUpVoice>> tonicToHarmony;

    private RuleParser(List<WarmUpVoice> drums, Map<NoteSymbol, List<WarmUpVoice>> tonicToHarmony) {
        this.drums = drums;
        this.tonicToHarmony = tonicToHarmony;
    }

    public static RuleParser valueOf(String str) {
        str = str.trim();
        String[] rules = str.split("\n");
        List<WarmUpVoice> drums = new ArrayList<>();
        Map<NoteSymbol, List<WarmUpVoice>> tonicToHarmony = new HashMap<>();

        for (String rule : rules) {
            rule = rule.trim();
            int voiceStart = rule.indexOf("[");
            String ruleFor = rule.substring(0, voiceStart).trim();
            String voices = rule.substring(voiceStart + 1, rule.length() - 1);
            List<WarmUpVoice> harmony = Harmony.valueOf(voices).getVoices();

            if (ruleFor.equals(DRUM_RULES)) {
                drums = harmony;
            } else {
                NoteSymbol tonic = NoteSymbol.getByCode(ruleFor);
                tonicToHarmony.put(tonic, harmony);
            }
        }
        return new RuleParser(drums, tonicToHarmony);
    }

    List<WarmUpVoice> getDrums() {
        return drums;
    }

    Map<NoteSymbol, List<WarmUpVoice>> getHarmony() {
        return tonicToHarmony;
    }

    @Override
    public String toString() {
        return "RuleParser{" +
                "drums=" + drums +
                ", tonicToHarmony=" + tonicToHarmony +
                '}';
    }
}
