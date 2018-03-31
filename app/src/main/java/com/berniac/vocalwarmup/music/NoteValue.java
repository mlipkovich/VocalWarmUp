package com.berniac.vocalwarmup.music;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mikhail Lipkovich on 11/17/2017.
 */
public class NoteValue {
    public static final NoteValue WHOLE = new NoteValue(1, 1);
    public static final NoteValue WHOLE_DOTTED = new NoteValue(3, 2);
    public static final NoteValue HALF = new NoteValue(1, 2);
    public static final NoteValue HALF_DOTTED = new NoteValue(3, 4);
    public static final NoteValue QUARTER = new NoteValue(1, 4);
    public static final NoteValue QUARTER_DOTTED = new NoteValue(3, 8);
    public static final NoteValue QUARTER_TRIOLE = new NoteValue(1, 6);
    public static final NoteValue EIGHTH = new NoteValue(1, 8);
    public static final NoteValue EIGHTH_DOTTED = new NoteValue(3, 16);
    public static final NoteValue EIGHTH_TRIOLE = new NoteValue(1, 12);
    public static final NoteValue SIXTEETH = new NoteValue(1, 16);
    public static final NoteValue SIXTEETH_DOTTED = new NoteValue(3, 16);
    public static final NoteValue SIXTEETH_TRIOLE = new NoteValue(1, 24);
    public static final NoteValue THIRTYSECOND = new NoteValue(1, 32);
    public static final NoteValue THIRTYSECOND_DOTTED = new NoteValue(3, 64);
    public static final NoteValue THIRTYSECOND_TRIOLE = new NoteValue(1, 48);
    public static final NoteValue SIXTYFOURTH = new NoteValue(1, 64);

    private int numerator;
    private int denominator;

    private static final Map<String, NoteValue> VALUE_BY_CODE = new HashMap<>();
    static {
        VALUE_BY_CODE.put("1",   WHOLE);
        VALUE_BY_CODE.put("15",  WHOLE_DOTTED);
        VALUE_BY_CODE.put("2",   HALF);
        VALUE_BY_CODE.put("25",  HALF_DOTTED);
        VALUE_BY_CODE.put("4",   QUARTER);
        VALUE_BY_CODE.put("45",  QUARTER_DOTTED);
        VALUE_BY_CODE.put("43",  QUARTER_TRIOLE);
        VALUE_BY_CODE.put("8",   EIGHTH);
        VALUE_BY_CODE.put("85",  EIGHTH_DOTTED);
        VALUE_BY_CODE.put("83",  EIGHTH_TRIOLE);
        VALUE_BY_CODE.put("16",  SIXTEETH);
        VALUE_BY_CODE.put("165", SIXTEETH_DOTTED);
        VALUE_BY_CODE.put("163", SIXTEETH_TRIOLE);
        VALUE_BY_CODE.put("32",  THIRTYSECOND);
        VALUE_BY_CODE.put("325", THIRTYSECOND_DOTTED);
        VALUE_BY_CODE.put("323", THIRTYSECOND_TRIOLE);
        VALUE_BY_CODE.put("64",  SIXTYFOURTH);
    }
    
    public NoteValue(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public static NoteValue getByCode(String code) {
        return VALUE_BY_CODE.get(code);
    }

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public float getNumberOfQuarters() {
        return ((float) (numerator * NoteValue.QUARTER.getDenominator()))
                / (denominator * NoteValue.QUARTER.getNumerator());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteValue noteValue = (NoteValue) o;

        return numerator == noteValue.numerator && denominator == noteValue.denominator;
    }

    @Override
    public int hashCode() {
        int result = numerator;
        result = 31 * result + denominator;
        return result;
    }

    @Override
    public String toString() {
        return "NoteValue{" +
                "numerator=" + numerator +
                ", denominator=" + denominator +
                '}';
    }
}
