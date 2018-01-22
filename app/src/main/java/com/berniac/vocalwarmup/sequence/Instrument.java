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
    ACORDEON("Ac"),
    DRUMS("Dr"),
    BASS_GUITAR("Ba"),
//    ACOUSTIC_GUITAR("Ac"),
    CLEAN_GUITAR("Gu"),
    OVERDRIVE_GUITAR("Ov"),
    CLASSIC_GUITAR_STRUMMING_1("C1"),
    CLASSIC_GUITAR_STRUMMING_2("C1"),
    CLASSIC_GUITAR_STRUMMING_3("C1"),
    CLASSIC_GUITAR_STRUMMING_4("C1"),
    CLASSIC_GUITAR_STRUMMING_5("C1"),
    CLASSIC_GUITAR_STRUMMING_6("C1"),
    FLUTE("Fl"),
    CLARINET("Cl"),
    HOBOJ("Ho"),
    SAXOPHONE("Sa"),
    FAGOT("Fa");
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
