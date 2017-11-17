package com.berniac.vocalwarmup.sequence;

import java.util.Collections;
import java.util.List;

/**
 * Created by Mikhail Lipkovich on 11/17/2017.
 */
public class Melody implements Playable {

    private WarmUpVoice melody;

    public Melody(WarmUpVoice melody) {
        this.melody = melody;
    }

    public static Melody valueOf(String s) {
        // TODO: Marina parse strings using WarmUpVoice methods
        return null;
    }

    @Override
    public List<WarmUpVoice> getVoices() {
        return Collections.singletonList(melody);
    }
}
