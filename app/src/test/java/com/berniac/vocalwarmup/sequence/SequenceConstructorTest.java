package com.berniac.vocalwarmup.sequence;

import com.berniac.vocalwarmup.music.MusicalSymbol;
import com.berniac.vocalwarmup.music.Note;
import com.berniac.vocalwarmup.music.NoteRegister;
import com.berniac.vocalwarmup.music.NoteSymbol;
import com.berniac.vocalwarmup.music.NoteValue;

import junit.framework.Assert;

import org.junit.Test;

import jp.kshoji.javax.sound.midi.MidiEvent;
import jp.kshoji.javax.sound.midi.Sequence;
import jp.kshoji.javax.sound.midi.ShortMessage;
import jp.kshoji.javax.sound.midi.Track;

import static com.berniac.vocalwarmup.sequence.SequenceConstructor.MidiTrack.HARMONY;
import static com.berniac.vocalwarmup.sequence.SequenceConstructor.MidiTrack.MELODY;
import static com.berniac.vocalwarmup.sequence.SequenceConstructor.MidiTrack.METRONOME;
import static com.berniac.vocalwarmup.sequence.SequenceConstructor.MidiTrack.LYRICS;

/**
 * Created by Mikhail Lipkovich on 11/17/2017.
 */
public class SequenceConstructorTest {
    @Test
    public void testNoHarmonyOneDirection() throws Exception {
        WarmUp warmUp = new WarmUp();
        warmUp.setLowerNote(new NoteRegister(NoteSymbol.C, 0));
        warmUp.setUpperNote(new NoteRegister(NoteSymbol.C, 1));
        warmUp.setStartingNote(new NoteRegister(NoteSymbol.C, 0));
        warmUp.setDirections(new Direction[]{Direction.START_TO_UPPER});
        warmUp.setRunMelody(true);

        MusicalSymbol[] musicalSymbols = new MusicalSymbol[]{
                new Note(NoteSymbol.C, 0, NoteValue.QUARTER),
                new Note(NoteSymbol.C_SHARP, 0, NoteValue.QUARTER),
                new Note(NoteSymbol.D, 0, NoteValue.QUARTER)
        };

        warmUp.setMelody(new Melody(new WarmUpVoice(musicalSymbols, Instrument.FORTEPIANO)));
        Sequence sequence = SequenceConstructor.construct(warmUp);

        int[] expectedMidiNotes = new int[]{36, 37, 38,
                                            38, 39, 40,
                                            40, 41, 42,
                                            42, 43, 44,
                                            44, 45, 46,
                                            46, 47, 48,
                                            48, 49, 50};

        long[] expectedPositions = new long[]{0,     96,    96,    96*2,  96*2,  96*3,
                                              96*3,  96*4,  96*4,  96*5,  96*5,  96*6,
                                              96*6,  96*7,  96*7,  96*8,  96*8,  96*9,
                                              96*9,  96*10, 96*10, 96*11, 96*11, 96*12,
                                              96*12, 96*13, 96*13, 96*14, 96*14, 96*15,
                                              96*15, 96*16, 96*16, 96*17, 96*17, 96*18,
                                              96*18, 96*19, 96*19, 96*20, 96*20, 96*21};

        Track[] tracks = sequence.getTracks();
        Assert.assertEquals(4, tracks.length);
        Assert.assertEquals(0, tracks[HARMONY.getIndex()].size());
        Assert.assertEquals(0, tracks[LYRICS.getIndex()].size());
        Assert.assertEquals(0, tracks[METRONOME.getIndex()].size());

        for (int i = 0; i < tracks[MELODY.getIndex()].size(); i++) {
            MidiEvent event = tracks[MELODY.getIndex()].get(i);
            int midiNote = ((ShortMessage)event.getMessage()).getData1();
            long midiPosition = event.getTick();
            int expectedMidiNote = expectedMidiNotes[Math.round(i / 2)];
            Assert.assertEquals(expectedMidiNote, midiNote);
            Assert.assertEquals(expectedPositions[i], midiPosition);
        }
    }

    // TODO: Marina and Mikhail add much more tests
}
