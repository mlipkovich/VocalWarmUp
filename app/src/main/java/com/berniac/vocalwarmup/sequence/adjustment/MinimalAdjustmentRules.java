package com.berniac.vocalwarmup.sequence.adjustment;

import com.berniac.vocalwarmup.music.MusicalSymbol;
import com.berniac.vocalwarmup.music.Note;
import com.berniac.vocalwarmup.music.NoteSymbol;
import com.berniac.vocalwarmup.music.NoteValue;
import com.berniac.vocalwarmup.music.Rest;
import com.berniac.vocalwarmup.sequence.WarmUpVoice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mikhail Lipkovich on 12/23/2017.
 */
public class MinimalAdjustmentRules implements AdjustmentRules {

    private RuleParser ruleParser;

    private MinimalAdjustmentRules(RuleParser ruleParser) {
        this.ruleParser = ruleParser;
    }

    public static MinimalAdjustmentRules valueOf(String str) {
        int rulesStart = str.indexOf("<");
        int rulesEnd = str.indexOf(">");
        RuleParser ruleParser = RuleParser.valueOf(str.substring(rulesStart + 1, rulesEnd));
        return new MinimalAdjustmentRules(ruleParser);
    }

    @Override
    public Map<NoteSymbol, List<WarmUpVoice>> getAdjustmentRules(int pauseQuartersCount) {
        List<WarmUpVoice> drums = ruleParser.getDrums();
        List<WarmUpVoice> resultingDrums = new ArrayList<>();
        for (WarmUpVoice drumVoice : drums) {
            List<MusicalSymbol> resultingVoiceSymbols = new ArrayList<>();
            List<MusicalSymbol> voiceSymbols = drumVoice.getMusicalSymbols();
            int symbolIndex = 0;
            float voiceQuartersCount = 0;
            while ((int) voiceQuartersCount < pauseQuartersCount) {
                // it is guaranteed that voices length should perfectly match so there is no need
                // to check whether we exceeded boundaries additionally
                MusicalSymbol currentSymbol = voiceSymbols.get(symbolIndex);
                resultingVoiceSymbols.add(currentSymbol);
                voiceQuartersCount += currentSymbol.getNoteValue().getNumberOfQuarters();
                symbolIndex++;

                // Pause is longer than specified symbols. Loop
                if (symbolIndex == voiceSymbols.size()) {
                    symbolIndex = 0;
                }
            }
            resultingDrums.add(new WarmUpVoice(resultingVoiceSymbols, drumVoice.getInstrument()));
        }

        Map<NoteSymbol, List<WarmUpVoice>> harmonies = ruleParser.getHarmony();
        Map<NoteSymbol, List<WarmUpVoice>> resultingRules = new HashMap<>();

        for (Map.Entry<NoteSymbol, List<WarmUpVoice>> harmonyRule : harmonies.entrySet()) {
            List<WarmUpVoice> voices = harmonyRule.getValue();
            List<WarmUpVoice> resultingVoices = new ArrayList<>(voices.size());
            for (WarmUpVoice voice : voices) {
                int originalQuartersCount = numberOfQuarters(voice);
                List<MusicalSymbol> weightedSymbols = new ArrayList<>();
                for (MusicalSymbol symbol : voice.getMusicalSymbols()) {
                    MusicalSymbol weightedSymbol;
                    NoteValue weightedNoteValue = new NoteValue(
                            symbol.getNoteValue().getNumerator() * pauseQuartersCount,
                            symbol.getNoteValue().getDenominator() * originalQuartersCount);

                    if (symbol.isSounding()) {
                        weightedSymbol = new Note(((Note) symbol).getNoteRegister(), weightedNoteValue);
                    } else {
                        weightedSymbol = new Rest(weightedNoteValue);
                    }
                    weightedSymbols.add(weightedSymbol);
                }
                WarmUpVoice weightedVoice = new WarmUpVoice(weightedSymbols, voice.getInstrument());
                resultingVoices.add(weightedVoice);
            }
            resultingRules.put(harmonyRule.getKey(), resultingVoices);
        }

        for (List<WarmUpVoice> voices : resultingRules.values()) {
            voices.addAll(resultingDrums);
        }
        return resultingRules;
    }

    private static int numberOfQuarters(WarmUpVoice voice) {
        float numberOfQuarters = 0;
        for (MusicalSymbol symbol : voice.getMusicalSymbols()) {
            NoteValue noteValue = symbol.getNoteValue();
            numberOfQuarters += noteValue.getNumberOfQuarters();
        }
        return (int)numberOfQuarters;
    }
}
