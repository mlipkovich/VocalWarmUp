package com.berniac.vocalwarmup.music;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Mikhail Lipkovich on 2/10/2018.
 */
public class NoteRegisterTest {
    @Test
    public void testValueOf() {
        Assert.assertEquals(new NoteRegister(NoteSymbol.D_BEMOL, -1), NoteRegister.valueOf("Des-1"));
        Assert.assertEquals(new NoteRegister(NoteSymbol.D, -1), NoteRegister.valueOf("D-1"));
        Assert.assertEquals(new NoteRegister(NoteSymbol.D, 2), NoteRegister.valueOf("D2"));
        Assert.assertEquals(new NoteRegister(NoteSymbol.D_BEMOL, 0), NoteRegister.valueOf("Des"));
        Assert.assertEquals(new NoteRegister(NoteSymbol.D, 0), NoteRegister.valueOf("D"));
    }
}
