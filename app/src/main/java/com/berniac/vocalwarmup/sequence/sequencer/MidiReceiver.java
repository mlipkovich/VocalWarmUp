package com.berniac.vocalwarmup.sequence.sequencer;

import com.berniac.vocalwarmup.midi.MidiUtils;
import com.berniac.vocalwarmup.midi.SF2Sequencer;
import com.berniac.vocalwarmup.music.NoteValue;
import com.berniac.vocalwarmup.sequence.Instrument;

import jp.kshoji.javax.sound.midi.InvalidMidiDataException;
import jp.kshoji.javax.sound.midi.MidiMessage;
import jp.kshoji.javax.sound.midi.Receiver;
import jp.kshoji.javax.sound.midi.ShortMessage;

/**
 * Created by Mikhail Lipkovich on 8/17/2018.
 */
public class MidiReceiver {

    private static final int DEFAULT_BPM = 120;
    private static final int DEFAULT_VOLUME = 100;
    private static final float DEFAULT_TEMPO_FACTOR = 1;

    private Receiver receiver;

    private volatile int harmonyVolume;
    private int previousHarmonyVolume;

    private volatile int adjustmentVolume;
    private int previousAdjustmentVolume;


    private volatile float tempoFactor;
    private volatile int tempoBpm;

    public MidiReceiver(Receiver receiver) {
        this.receiver = receiver;
        initDefaults();
    }

    public void initDefaults() {
        this.harmonyVolume = DEFAULT_VOLUME;
        this.previousHarmonyVolume = DEFAULT_VOLUME;
        this.adjustmentVolume = DEFAULT_VOLUME;
        this.previousAdjustmentVolume = DEFAULT_VOLUME;

        this.tempoFactor = DEFAULT_TEMPO_FACTOR;
        this.tempoBpm = DEFAULT_BPM;

        updateHarmonyVolume(true);
        updateAdjustmentVolume(true);
        updateMelodyVolume(DEFAULT_VOLUME);
    }

    public void setTempoFactor(float tempoFactor) {
        this.tempoFactor = tempoFactor;
    }

    public void setTempo(int tempoBpm) {
        this.tempoBpm = tempoBpm;
    }

    public void setHarmonyVolume(int volume) {
        this.harmonyVolume = volume;
    }

    public void setAdjustmentVolume(int adjustmentVolume) {
        this.adjustmentVolume = adjustmentVolume;
    }

    public void playEvent(MidiMessage message) {
        receiver.send(message, 0);
    }

    public long timeRemainedBeforeEvent(long eventTick, long tickPosition) {
        return (long) ((1.0f / getTicksPerMicrosecond()) *
                (eventTick - tickPosition) / 1000f / tempoFactor);
    }

    private float getTicksPerMicrosecond() {
        int ticksPerBeat = MidiUtils.getNoteValueInTicks(NoteValue.QUARTER);
        float beatsPerSecond = tempoBpm / 60f;
        float microsecondPerSecond = 1000000.0f;

        return (beatsPerSecond * ticksPerBeat) / microsecondPerSecond;
    }

    public void updateMelodyVolume(int melodyVolume) {
        try {
//            if (melodyVolume != 0) {
//                melodyVolume = (int)(((Math.exp(6.908*(((float) melodyVolume) / 127.)))/1000.)*127.);
//            }
            int channel = SF2Sequencer.getChannel(Instrument.MELODIC_VOICE);
            // TODO: very dirty.
            System.out.println("Melody volume " + melodyVolume);
            receiver.send(new ShortMessage(ShortMessage.CONTROL_CHANGE | channel, 7, melodyVolume), 0);
        } catch (InvalidMidiDataException ignored) {
        }
    }

    public void updateHarmonyVolume(boolean force) {
        if (force || previousHarmonyVolume != harmonyVolume) {
            previousHarmonyVolume = harmonyVolume;
            try {
                int channel = SF2Sequencer.getChannel(Instrument.FORTEPIANO);
                receiver.send(new ShortMessage(ShortMessage.CONTROL_CHANGE | channel, 7, previousHarmonyVolume), 0);
            } catch (InvalidMidiDataException ignored) {
            }
        }
    }

    public void updateAdjustmentVolume(boolean force) {
        if (force || previousAdjustmentVolume != adjustmentVolume) {
            previousAdjustmentVolume = adjustmentVolume;
            try {
                int channel = SF2Sequencer.getChannel(Instrument.FORTEPIANO);
                receiver.send(new ShortMessage(ShortMessage.CONTROL_CHANGE | channel, 7, previousAdjustmentVolume), 0);
            } catch (InvalidMidiDataException ignored) {
            }
        }
    }

    public void muteAll() {
        try {
            // TODO: Do it for all channels
            receiver.send(new ShortMessage(0xb0, 0x7B, 0),
                    -1);
            receiver.send(new ShortMessage(0xb1, 0x7B, 0),
                    -1);
        } catch (InvalidMidiDataException ignored) {
        }
    }
}
