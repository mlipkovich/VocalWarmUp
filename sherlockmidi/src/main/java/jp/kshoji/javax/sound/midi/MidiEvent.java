package jp.kshoji.javax.sound.midi;

import android.support.annotation.NonNull;

/**
 * Represents MIDI Event
 * 
 * @author K.Shoji
 */
public class MidiEvent {
    private final MidiMessage message;
    
    private long tick;
   
    /**
     * Constructor
     * 
     * @param message the message
     * @param tick -1 if timeStamp not supported.
     */
    public MidiEvent(@NonNull final MidiMessage message, final long tick) {
        this.message = message;
        this.tick = tick;
    }

    /**
     * Get the {@link MidiDevice} of this {@link MidiEvent}
     * 
     * @return the {@link MidiDevice} of this {@link MidiEvent}
     */
    @NonNull
    public MidiMessage getMessage() {
        return message;
    }

    /**
     * Get the timeStamp in tick
     *
     * @return -1 if timeStamp not supported.
     */
    public long getTick() {
        return tick;
    }

    /**
     * Set the timeStamp in tick
     * 
     * @param tick timeStamp
     */
    public void setTick(long tick) {
        this.tick = tick;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MidiEvent event = (MidiEvent) o;

        return tick == event.tick && message.equals(event.message);

    }

    @Override
    public int hashCode() {
        int result = message.hashCode();
        result = 31 * result + (int) (tick ^ (tick >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "MidiEvent{" +
                "message=" + message +
                ", tick=" + tick +
                '}';
    }
}