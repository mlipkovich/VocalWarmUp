package com.berniac.vocalwarmup.sequence;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mikhail Lipkovich on 11/17/2017.
 */
public enum Instrument {
    MELODIC_VOICE("Me"),
    VOCAL_VOICE("Vo"),
    FORTEPIANO("Fo"),
    ELECTROPIANO("El"),
    ORGAN("Or"),
    ACORDEON("Ac");
    // TODO: Add other instruments

    private String code;
    private static final Map<String, Instrument> VALUE_BY_CODE = new HashMap<>();
    static {
        for (Instrument instrument : Instrument.values()) {
            VALUE_BY_CODE.put(instrument.code, instrument);
        }
    }

    Instrument(String code) {
        this.code = code;
    }

    public static Instrument getByCode(String code) {
        Instrument instrument = VALUE_BY_CODE.get(code);
        if (instrument == null) {
            throw new IllegalArgumentException("Unknown instrument code " + code);
        }
        return VALUE_BY_CODE.get(code);
    }
}
