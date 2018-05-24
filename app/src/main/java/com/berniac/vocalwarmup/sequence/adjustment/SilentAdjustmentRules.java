package com.berniac.vocalwarmup.sequence.adjustment;

import com.berniac.vocalwarmup.music.MusicalSymbol;
import com.berniac.vocalwarmup.music.Note;
import com.berniac.vocalwarmup.music.NoteRegister;
import com.berniac.vocalwarmup.music.NoteSymbol;
import com.berniac.vocalwarmup.music.NoteValue;
import com.berniac.vocalwarmup.music.Rest;
import com.berniac.vocalwarmup.sequence.Instrument;
import com.berniac.vocalwarmup.sequence.WarmUpVoice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mikhail Lipkovich on 12/24/2017.
 */
public class SilentAdjustmentRules implements AdjustmentRules {

    private SilentAdjustmentRules(){}

    public static SilentAdjustmentRules valueOf(String str) {
        return new SilentAdjustmentRules();
    }

    @Override
    public Map<NoteSymbol, List<WarmUpVoice>> getAdjustmentRules(int pauseQuartersCount) {
        List<MusicalSymbol> symbols = new ArrayList<>(pauseQuartersCount);
        for (int quarter = 0; quarter < pauseQuartersCount; quarter++) {
            symbols.add(new Rest(NoteValue.QUARTER));
        }

        List<WarmUpVoice> voice = Collections.singletonList(new WarmUpVoice(symbols, Instrument.MELODIC_VOICE));
        Map<NoteSymbol, List<WarmUpVoice>> voices = new HashMap<>();
        for (NoteSymbol noteSymbol : NoteSymbol.values()) {
            voices.put(noteSymbol, voice);
        }
        return voices;
    }
}
