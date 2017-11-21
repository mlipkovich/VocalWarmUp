package com.berniac.vocalwarmup.sequence;

import java.util.List;

/**
 * Created by Mikhail Lipkovich on 11/17/2017.
 */
public class Harmony implements Playable {

    private List<WarmUpVoice> harmony;

    Harmony(List<WarmUpVoice> harmony) {
        this.harmony = harmony;
    }

    public static Harmony valueOf() {
        // TODO: Marina parse strings using WarmUpVoice methods
        return null;
    }

    @Override
    public List<WarmUpVoice> getVoices() {
        return harmony;
    }
}
