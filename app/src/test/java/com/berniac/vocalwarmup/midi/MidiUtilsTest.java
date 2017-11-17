package com.berniac.vocalwarmup.midi;

import com.berniac.vocalwarmup.midi.MidiUtils;
import com.berniac.vocalwarmup.music.NoteRegister;
import com.berniac.vocalwarmup.music.NoteSymbol;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Mikhail Lipkovich on 11/17/2017.
 */
public class MidiUtilsTest {
    @Test
    public void testLowerNoteConversion() {
        NoteRegister note = new NoteRegister(NoteSymbol.C, -3);
        Assert.assertEquals(0, MidiUtils.getMidiNote(note));
    }

    @Test
    public void testUpperNoteConversion() {
        NoteRegister note = new NoteRegister(NoteSymbol.H, 5);
        Assert.assertEquals(107, MidiUtils.getMidiNote(note));
    }

    @Test
    public void testCBemolConversion() {
        NoteRegister note = new NoteRegister(NoteSymbol.C_BEMOL, 0);
        Assert.assertEquals(35, MidiUtils.getMidiNote(note));
    }

    @Test
    public void testHSharpConversion() {
        NoteRegister note = new NoteRegister(NoteSymbol.H_SHARP, 0);
        Assert.assertEquals(48, MidiUtils.getMidiNote(note));
    }

    @Test
    public void testTranspose() {
        Assert.assertEquals(0, MidiUtils.transpose(new NoteRegister(NoteSymbol.C, 0), 0));
        Assert.assertEquals(20, MidiUtils.transpose(new NoteRegister(NoteSymbol.C, 0), 20));
        Assert.assertEquals(19, MidiUtils.transpose(new NoteRegister(NoteSymbol.C_BEMOL, 0), 20));
        Assert.assertEquals(32, MidiUtils.transpose(new NoteRegister(NoteSymbol.H_SHARP, 0), 20));
        Assert.assertEquals(55, MidiUtils.transpose(new NoteRegister(NoteSymbol.C_BEMOL, 3), 20));
        Assert.assertEquals(68, MidiUtils.transpose(new NoteRegister(NoteSymbol.H_SHARP, 3), 20));
    }
}
