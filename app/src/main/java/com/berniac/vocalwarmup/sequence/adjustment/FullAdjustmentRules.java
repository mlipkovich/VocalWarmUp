package com.berniac.vocalwarmup.sequence.adjustment;

import android.util.SparseArray;

import com.berniac.vocalwarmup.music.NoteSymbol;
import com.berniac.vocalwarmup.sequence.WarmUpVoice;

import java.util.List;
import java.util.Map;

/**
 * Created by Mikhail Lipkovich on 12/23/2017.
 */
public class FullAdjustmentRules implements AdjustmentRules {

    private static final String FULL_ADJUSTMENT_MARK = "Full";
    private SparseArray<RuleParser> pauseSizeToAdjustment;

    private FullAdjustmentRules(SparseArray<RuleParser> pauseSizeToAdjustment){
        this.pauseSizeToAdjustment = pauseSizeToAdjustment;
    }

    public static FullAdjustmentRules valueOf(String str) {

        int fromIndex = 0;
        int adjustmentStart = str.indexOf(FULL_ADJUSTMENT_MARK, fromIndex);
        SparseArray<RuleParser> pauseSizeToAdjustment = new SparseArray<>();

        do {
            int ruleStart = str.indexOf("<", adjustmentStart);
            int ruleEnd = str.indexOf(">", adjustmentStart);
            String ruleStr = str.substring(ruleStart + 1, ruleEnd);
            String pauseSizeStr = str.substring(
                    adjustmentStart + FULL_ADJUSTMENT_MARK.length(), ruleStart);
            int pauseSize = Integer.valueOf(pauseSizeStr);

            RuleParser rule = RuleParser.valueOf(ruleStr);
            pauseSizeToAdjustment.put(pauseSize, rule);

            fromIndex = ruleEnd;
            adjustmentStart = str.indexOf(FULL_ADJUSTMENT_MARK, fromIndex);
        } while (fromIndex < str.length() && adjustmentStart != -1);

        return new FullAdjustmentRules(pauseSizeToAdjustment);
    }

    @Override
    public Map<NoteSymbol, List<WarmUpVoice>> getAdjustmentRules(int pauseQuartersCount) {
        RuleParser rules = pauseSizeToAdjustment.get(pauseQuartersCount);
        Map<NoteSymbol, List<WarmUpVoice>> allVoices = rules.getHarmony();
        List<WarmUpVoice> drums = rules.getDrums();
        for (List<WarmUpVoice> voices : allVoices.values()) {
            voices.addAll(drums);
        }
        return allVoices;
    }
}
