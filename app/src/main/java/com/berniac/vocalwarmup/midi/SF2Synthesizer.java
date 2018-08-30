package com.berniac.vocalwarmup.midi;

import com.berniac.vocalwarmup.sequence.Instrument;
import com.berniac.vocalwarmup.sequence.sequencer.MidiReceiver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.sherlock.com.sun.media.sound.SF2Soundbank;
import cn.sherlock.com.sun.media.sound.SoftSynthesizer;
import jp.kshoji.javax.sound.midi.MidiUnavailableException;

/**
 * Created by Mikhail Lipkovich on 11/9/2017.
 * Stores global synthesizer based on provided SF2 banks file
 */
public class SF2Synthesizer {

    private static volatile boolean isConfigured;
    private static SoftSynthesizer synthesizer;
    private static Map<Instrument, SF2Database.Program> programs;

    private static Map<Instrument, Integer> instrumentToChannel = new HashMap<>();

    private SF2Synthesizer(){}

    public static void configure(SF2Soundbank soundbank, Map<Instrument, SF2Database.Program> programs) {
        try {
            synthesizer = new SoftSynthesizer();
            synthesizer.open();
            synthesizer.loadAllInstruments(soundbank);
            SF2Synthesizer.programs = programs;

            isConfigured = true;
        } catch (MidiUnavailableException e) {
            throw new IllegalStateException("Failed to open synthesizer", e);
        }
    }

    public static void reassignChannels(Set<Instrument> instruments) {
        if (!isConfigured) {
            throw new IllegalStateException("SF2Synthesizer should be configured first");
        }
        int channel = 0;
        for (Instrument instrument : instruments) {
            SF2Database.Program program = programs.get(instrument);
            synthesizer.getChannels()[channel].programChange(program.getBank(), program.getProgram());
            instrumentToChannel.put(instrument, channel);
            channel++;
        }
    }

    public static MidiReceiver getReceiver() {
        if (!isConfigured) {
            throw new IllegalStateException("SF2Synthesizer should be configured first");
        }
        try {
            return new MidiReceiver(synthesizer.getReceiver(), instrumentToChannel);
        } catch (MidiUnavailableException e) {
            throw new RuntimeException("Failed to get receiver", e);
        }
    }
}
