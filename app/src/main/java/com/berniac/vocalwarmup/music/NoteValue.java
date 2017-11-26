package com.berniac.vocalwarmup.music;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mikhail Lipkovich on 11/17/2017.
 */
public enum NoteValue {
    WHOLE("1"),
    HALF("2"),
    HALF_DOTTED("25"),
    QUARTER("4"),
    QUARTER_DOTTED("45"),
    QUARTER_TRIOLE("43"),
    EIGHTH("8"),
    EIGHTH_DOTTED("85"),
    EIGHTH_TRIOLE("83"),
    SIXTEETH("16"),
    SIXTEETH_DOTTED("165"),
    SIXTEETH_TRIOLE("163"),
    THIRTYSECOND("32"),
    THIRTYSECOND_DOTTED("325"),
    THIRTYSECOND_TRIOLE("323"),
    SIXTYFOURTH("64");

    private String code;
    private static final Map<String, NoteValue> VALUE_BY_CODE = new HashMap<>();
    static {
        for (NoteValue noteValue : NoteValue.values()) {
            VALUE_BY_CODE.put(noteValue.code, noteValue);
        }
    }

    NoteValue(String code) {
        this.code = code;
    }

    public static NoteValue getByCode(String code) {
        return VALUE_BY_CODE.get(code);
    }
}
