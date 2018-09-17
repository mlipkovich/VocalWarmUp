package com.berniac.vocalwarmup.sequence.sequencer;

import com.berniac.vocalwarmup.midi.MidiUtils;
import com.berniac.vocalwarmup.music.NoteValue;
import com.berniac.vocalwarmup.sequence.Instrument;

import java.util.Map;

import jp.kshoji.javax.sound.midi.InvalidMidiDataException;
import jp.kshoji.javax.sound.midi.MidiMessage;
import jp.kshoji.javax.sound.midi.Receiver;
import jp.kshoji.javax.sound.midi.ShortMessage;

/**
 * Created by Mikhail Lipkovich on 8/17/2018.
 */
public class MidiReceiver {

    private static final int DEFAULT_BPM = 112;
    private static final float DEFAULT_TEMPO_FACTOR = 1;

    // TODO: Instrument -> Volume mapping
    private static final int DEFAULT_VOLUME = 90;
    private static final int MELODY_DEFAULT_VOLUME = 100;
    private static final int METRONOME_DEFAULT_VOLUME = 0;

    private static final int MIDI_CHANNEL_VOLUME = 0x7;
    private static final int MIDI_ALL_NOTE_OFF = 0x7b;

    private Receiver receiver;
    private Map<Instrument, Integer> instrumentToChannel;

    private volatile int harmonyVolume;
    private int previousHarmonyVolume;

    private volatile int adjustmentVolume;
    private int previousAdjustmentVolume;

    private volatile float tempoFactor;
    private volatile int tempoBpm;

    public MidiReceiver(Receiver receiver, Map<Instrument, Integer> instrumentToChannel) {
        this.instrumentToChannel = instrumentToChannel;
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
        updateMelodyVolume(MELODY_DEFAULT_VOLUME);
        updateMetronomeVolume(METRONOME_DEFAULT_VOLUME);
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

    public void playEvent(MidiEventShort midiEvent) {
        try {
            int channel = instrumentToChannel.get(midiEvent.getInstrument());
            MidiMessage message = new ShortMessage(midiEvent.getEventType(), channel,
                    midiEvent.getNote(), DEFAULT_VOLUME);
            receiver.send(message, 0);
        } catch (InvalidMidiDataException e) {
            onInvalidMidiData(e);
        }
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

    public void updateMelodyVolume(int volume) {
        int channel = instrumentToChannel.get(Instrument.MELODIC_VOICE);
        changeChannelVolume(channel, volume);
    }

    public void updateMetronomeVolume(int volume) {
        int channel = instrumentToChannel.get(Instrument.METRONOME);
        changeChannelVolume(channel, volume);
    }

    public void updateHarmonyVolume(boolean force) {
        if (force || previousHarmonyVolume != harmonyVolume) {
            previousHarmonyVolume = harmonyVolume;
            changeAccompanimentVolume(previousHarmonyVolume);
        }
    }

    public void updateAdjustmentVolume(boolean force) {
        if (force || previousAdjustmentVolume != adjustmentVolume) {
            previousAdjustmentVolume = adjustmentVolume;
            changeAccompanimentVolume(previousAdjustmentVolume);
        }
    }

    public void muteAll() {
        for (int channel : instrumentToChannel.values()) {
            try {
                MidiMessage message = new ShortMessage(ShortMessage.CONTROL_CHANGE | channel,
                        MIDI_ALL_NOTE_OFF, 0);
                receiver.send(message, -1);
            } catch (InvalidMidiDataException e) {
                onInvalidMidiData(e);
            }
        }
    }

    private void changeAccompanimentVolume(int volume) {
        for (Map.Entry<Instrument, Integer> instrumentChannel : instrumentToChannel.entrySet()) {
            if ((instrumentChannel.getKey() == Instrument.MELODIC_VOICE) ||
                    (instrumentChannel.getKey() == Instrument.METRONOME)) {
                continue;
            }
            int channel = instrumentChannel.getValue();
            changeChannelVolume(channel, volume);
        }
    }

    private void changeChannelVolume(int channel, int volume) {
        try {
            MidiMessage message = new ShortMessage(ShortMessage.CONTROL_CHANGE | channel,
                    MIDI_CHANNEL_VOLUME, volume);
            receiver.send(message, 0);
        } catch (InvalidMidiDataException e) {
            onInvalidMidiData(e);
        }
    }

    private void onInvalidMidiData(InvalidMidiDataException e) {
        throw new IllegalStateException("Failed to process midi data", e);
    }
}
