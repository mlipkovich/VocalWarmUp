package com.berniac.vocalwarmup.sequence.adjustment;

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
}
