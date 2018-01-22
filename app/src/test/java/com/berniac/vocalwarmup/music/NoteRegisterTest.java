package com.berniac.vocalwarmup.music;

import org.junit.Assert;
import org.junit.Test;

import static com.berniac.vocalwarmup.music.NoteRegister.semitonesBetween;

/**
 * Created by Marina Gorlova on 21.12.2017.
 */
public class NoteRegisterTest {
    @Test
    public void testSemitonesBetween() throws Exception {
        NoteRegister noteRegisterFrom = new NoteRegister(NoteSymbol.C, 0);
        NoteRegister noteRegisterTo = new NoteRegister(NoteSymbol.C, 0);
        Assert.assertEquals(semitonesBetween(noteRegisterFrom, noteRegisterTo), 0);

        noteRegisterFrom = new NoteRegister(NoteSymbol.C, 0);
        noteRegisterTo = new NoteRegister(NoteSymbol.C, 3);
        Assert.assertEquals(semitonesBetween(noteRegisterFrom, noteRegisterTo), 36);


        noteRegisterFrom = new NoteRegister(NoteSymbol.C, -1);
        noteRegisterTo = new NoteRegister(NoteSymbol.C, 1);
        Assert.assertEquals(semitonesBetween(noteRegisterFrom, noteRegisterTo), 24);

        noteRegisterFrom = new NoteRegister(NoteSymbol.F, -1);
        noteRegisterTo = new NoteRegister(NoteSymbol.C_SHARP, -1);
        Assert.assertEquals(semitonesBetween(noteRegisterFrom, noteRegisterTo), -4);
    }
}
