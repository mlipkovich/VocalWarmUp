package com.berniac.vocalwarmup.sequence.adjustment;

import com.berniac.vocalwarmup.music.NoteSymbol;
import com.berniac.vocalwarmup.sequence.WarmUpVoice;

import java.util.List;
import java.util.Map;

/**
 * Created by Mikhail Lipkovich on 12/20/2017.
 */
public interface AdjustmentRules {
    Map<NoteSymbol, List<WarmUpVoice>> getAdjustmentRules(int pauseQuartersCount);
}
