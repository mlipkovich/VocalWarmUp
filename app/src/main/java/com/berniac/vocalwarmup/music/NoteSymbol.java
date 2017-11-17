package com.berniac.vocalwarmup.music;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mikhail Lipkovich on 11/17/2017.
 */
public enum NoteSymbol {
    C("C"),
    C_SHARP("Cis"),
    C_BEMOL("Ces"),
    D("D"),
    D_SHARP("Dis"),
    D_BEMOL("Des"),
    E("E"),
    E_SHARP("Eis"),
    E_BEMOL("Ees"),
    F("F"),
    F_SHARP("Fis"),
    F_BEMOL("Fes"),
    G("G"),
    G_SHARP("Gis"),
    G_BEMOL("Ges"),
    A("A"),
    A_SHARP("Ais"),
    A_BEMOL("Aes"),
    H("H"),
    H_SHARP("His"),
    H_BEMOL("Hes");

    private String code;

    private static final Map<String, NoteSymbol> VALUE_BY_CODE = new HashMap<>();
    static {
        for (NoteSymbol noteSymbol : NoteSymbol.values()) {
            VALUE_BY_CODE.put(noteSymbol.code, noteSymbol);
        }
    }

    NoteSymbol(String code) {
        this.code = code;
    }

    public NoteSymbol getByCode(String code) {
        return VALUE_BY_CODE.get(code);
    }
}
