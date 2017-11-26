package com.berniac.vocalwarmup.music;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Mikhail Lipkovich on 11/17/2017.
 */
public class MusicSymbolParserTest {
    @Test
    public void testParsingNote() throws Exception {
        MusicalSymbol note = new Note(NoteSymbol.C, 0, NoteValue.EIGHTH);
        Assert.assertEquals(note, MusicalSymbolParser.parse("8C"));

        note = new Note(NoteSymbol.D_SHARP, -1, NoteValue.EIGHTH_TRIOLE);
        Assert.assertEquals(note, MusicalSymbolParser.parse("83Dis-1"));

        note = new Note(NoteSymbol.H, 3, NoteValue.QUARTER);
        Assert.assertEquals(note, MusicalSymbolParser.parse("4H3"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParsingWrongOctave() throws Exception {
        MusicalSymbolParser.parse("8H-9");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParsingWrongNoteSymbol() throws Exception {
        MusicalSymbolParser.parse("8Gil3");
    }

    @Test
    public void testParsingRest() throws Exception {
        MusicalSymbol note = new Rest(NoteValue.HALF_DOTTED);
        Assert.assertEquals(note, MusicalSymbolParser.parse("25N"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParsingWrongRest() throws Exception {
        MusicalSymbolParser.parse("163N2");
    }
}
