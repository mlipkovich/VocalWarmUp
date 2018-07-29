package com.berniac.vocalwarmup.sequence.adjustment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikhail Lipkovich on 12/24/2017.
 */
public class AdjustmentRulesParser {
    private static final String FULL_ADJUSTMENT_MARK = "Full";
    private static final String MINIMAL_ADJUSTMENT_MARK = "Mid";
    private static final String SILENT_ADJUSTMENT_MARK = "Sil";

    public static AdjustmentRules parse(String str) {
        if (str.startsWith(FULL_ADJUSTMENT_MARK)) {
            return FullAdjustmentRules.valueOf(str);
        }
        if (str.startsWith(MINIMAL_ADJUSTMENT_MARK)) {
            return MinimalAdjustmentRules.valueOf(str);
        }
        if (str.startsWith(SILENT_ADJUSTMENT_MARK)) {
            return SilentAdjustmentRules.valueOf(str);
        }

        throw new IllegalArgumentException("Unknown adjustment type for string " + str);
    }

    public static List<AdjustmentRules> parseSeveral(String str) {
        List<AdjustmentRules> rules = new ArrayList<>();
        int ruleStart = 0;
        int ruleEnd;
        while ((ruleEnd = str.indexOf(">", ruleStart)) != -1) {
            String adjustmentStr = str.substring(ruleStart, ruleEnd + 1);
            rules.add(parse(adjustmentStr));
            ruleStart = ruleEnd + 1;
        }

        return rules;
    }
}
