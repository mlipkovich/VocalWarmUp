package com.berniac.vocalwarmup.sequence;

import com.berniac.vocalwarmup.sequence.Harmony;
import com.berniac.vocalwarmup.sequence.Instrument;
import com.berniac.vocalwarmup.sequence.OctaveShifts;
import com.berniac.vocalwarmup.sequence.adjustment.AdjustmentRules;
import com.berniac.vocalwarmup.sequence.adjustment.AdjustmentRulesParser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Mikhail Lipkovich on 2/25/2018.
 */
public class Accompaniment {

    private static final String ACCOMP_MARKER = "Acc";
    private static final String VOICES_MARKER = "Vo";
    private static final String HARMONY_MARKER = "Ha";
    private static final String ADJUSTMENT_MARKER = "Mo";

    private Map<Integer, Voice> voices;
    private Harmony harmony;
    private List<AdjustmentRules> adjustments;
    private int adjustmentMelodyIntersection;

    public static class Voice {
        private Instrument instrument;
        private OctaveShifts octaveShifts;

        public Voice(Instrument instrument, OctaveShifts octaveShifts) {
            this.instrument = instrument;
            this.octaveShifts = octaveShifts;
        }

        public Instrument getInstrument() {
            return instrument;
        }

        public OctaveShifts getOctaveShifts() {
            return octaveShifts;
        }

        @Override
        public String toString() {
            return "Voice{" +
                    "instrument=" + instrument +
                    ", octaveShifts=" + octaveShifts +
                    '}';
        }
    }

    Accompaniment(Map<Integer, Voice> voices, Harmony harmony,
                  List<AdjustmentRules> adjustments, int adjustmentMelodyIntersection) {
        this.voices = voices;
        this.harmony = harmony;
        this.adjustments = adjustments;
        this.adjustmentMelodyIntersection = adjustmentMelodyIntersection;
    }

    public static Accompaniment valueOf(String str) {
        if (!str.startsWith(ACCOMP_MARKER)) {
            throw new IllegalArgumentException("Provided string " + str + " is not accompaniment");
        }
        str = str.substring(ACCOMP_MARKER.length() + 1, str.length() - 1);

        String voicesStr = getSubProperty(str, VOICES_MARKER, VOICES_MARKER.length());
        Map<Integer, Voice> voices = parseVoices(voicesStr);

        String harmonyStr = getSubProperty(str, HARMONY_MARKER, HARMONY_MARKER.length());
        Harmony harmony = Harmony.valueOf(harmonyStr);

        // TODO: Ugly. Move intersection to separate property?
        int adjustmentStart = str.indexOf(ADJUSTMENT_MARKER);
        int intersectionStart = adjustmentStart + ADJUSTMENT_MARKER.length() + 1;
        int intersectionEnd = str.indexOf(")", intersectionStart);
        String intersectionStr = str.substring(intersectionStart, intersectionEnd);
        int intersection = Integer.valueOf(intersectionStr);

        String adjustmentStr = getSubProperty(str, ADJUSTMENT_MARKER,
                ADJUSTMENT_MARKER.length() + intersectionStr.length() + 2);
        List<AdjustmentRules> adjustmentRules = AdjustmentRulesParser.parseSeveral(adjustmentStr);

        return new Accompaniment(voices, harmony, adjustmentRules, intersection);
    }

    public Map<Integer, Voice> getVoices() {
        return voices;
    }

    public Harmony getHarmony() {
        return harmony;
    }

    public List<AdjustmentRules> getAdjustments() {
        return adjustments;
    }

    public int getAdjustmentMelodyIntersection() {
        return adjustmentMelodyIntersection;
    }

    private static String getSubProperty(String str, String marker, int markerSize) {
        int propertyStart = str.indexOf(marker);
        int propertyEnd = str.indexOf("}", propertyStart);
        return str.substring(propertyStart + markerSize + 1, propertyEnd).trim();
    }

    private static Map<Integer, Voice> parseVoices(String str) {
        Map<Integer, Voice> voices = new HashMap<>();
        // Zero voice is for melody
        voices.put(0, new Voice(Instrument.MELODIC_VOICE, OctaveShifts.EMPTY_SHIFTS));
        Scanner scanner = new Scanner(str);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] splitted = line.split("=");
            int number = Integer.valueOf(splitted[0]);
            String instrumentShifts = splitted[1];
            int shiftsIndex = instrumentShifts.indexOf("[");
            Instrument instrument = Instrument.getByCode(instrumentShifts.substring(0, shiftsIndex));
            OctaveShifts shifts = OctaveShifts.valueOfNew(instrumentShifts.substring(shiftsIndex));
            voices.put(number, new Voice(instrument, shifts));
        }
        return voices;
    }
}
