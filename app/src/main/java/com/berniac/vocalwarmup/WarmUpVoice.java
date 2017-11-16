package com.berniac.vocalwarmup;

/**
 * Created by Marina on 13.11.2017.
 */

public class WarmUpVoice {

    private int[] shifts;
    private int[] noteValueInTicks;
    private int channel;

    public WarmUpVoice(int[] shifts, int[] noteValueInTicks, int channel) {
        this.shifts = shifts;
        this.noteValueInTicks = noteValueInTicks;
        this.channel = channel;
    }

    public int[] getShifts() {
        return shifts;
    }

    public void setShifts(int[] shifts) {
        this.shifts = shifts;
    }

    public int[] getNoteValueInTicks() {
        return noteValueInTicks;
    }

    public void setNoteValueInTicks(int[] noteValueInTicks) {
        this.noteValueInTicks = noteValueInTicks;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }
}
