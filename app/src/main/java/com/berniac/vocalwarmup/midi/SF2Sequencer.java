package com.berniac.vocalwarmup.midi;

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

import cn.sherlock.com.sun.media.sound.SF2Soundbank;
import cn.sherlock.com.sun.media.sound.SoftSynthesizer;
import jp.kshoji.javax.sound.midi.MidiSystem;
import jp.kshoji.javax.sound.midi.MidiUnavailableException;
import jp.kshoji.javax.sound.midi.Receiver;
import jp.kshoji.javax.sound.midi.Sequencer;

/**
 * Created by Mikhail Lipkovich on 11/9/2017.
 * Stores global sequencer which is mapped to synthesizer based on provided SF2 banks file
 */
public class SF2Sequencer {

    private static volatile boolean isConfigured;
    private static Sequencer sequencer;

    private SF2Sequencer(){}

    public static void configure(InputStream stream) {
        try {
            SF2Soundbank sf = new SF2Soundbank(stream);

            SoftSynthesizer synthesizer = new SoftSynthesizer();
            synthesizer.open();
            synthesizer.loadAllInstruments(sf);

            // TODO: Move to config
            synthesizer.getChannels()[0].programChange(0);
            synthesizer.getChannels()[1].programChange(1);
            Receiver receiver = synthesizer.getReceiver();

            sequencer = MidiSystem.getSequencer();
            sequencer.setReceiver(receiver);
            isConfigured = true;
        } catch (IOException | MidiUnavailableException e) {
            throw new IllegalStateException("Failed to get sequencer", e);
        }
    }

    public static Sequencer getSequencer() {
        if (!isConfigured) {
            throw new IllegalStateException("SF2Sequencer should be configured first");
        }
        return sequencer;
    }
}
