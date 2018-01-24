package com.berniac.vocalwarmup.sequence;

import com.berniac.vocalwarmup.midi.MidiUtils;
import com.berniac.vocalwarmup.music.FixedStep;
import com.berniac.vocalwarmup.music.MusicalSymbol;
import com.berniac.vocalwarmup.music.Note;
import com.berniac.vocalwarmup.music.NoteRegister;
import com.berniac.vocalwarmup.music.NoteSymbol;
import com.berniac.vocalwarmup.music.NoteValue;
import com.berniac.vocalwarmup.music.Rest;
import com.berniac.vocalwarmup.sequence.adjustment.SilentAdjustmentRules;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jp.kshoji.javax.sound.midi.InvalidMidiDataException;
import jp.kshoji.javax.sound.midi.MidiEvent;
import jp.kshoji.javax.sound.midi.MidiTrackSpecificEvent;
import jp.kshoji.javax.sound.midi.Sequence;
import jp.kshoji.javax.sound.midi.ShortMessage;
import jp.kshoji.javax.sound.midi.Track;

/**
 * Created by Mikhail Lipkovich on 11/17/2017.
 */
public class SequenceConstructorTest {

    private WarmUpVoice voice1 = new WarmUpVoice(
            Arrays.asList(
                    new Note(new NoteRegister(NoteSymbol.F, 0), NoteValue.QUARTER),
                    new Rest(NoteValue.EIGHTH),
                    new Note(new NoteRegister(NoteSymbol.D, 0), NoteValue.QUARTER),
                    new Note(new NoteRegister(NoteSymbol.C, 0), NoteValue.QUARTER)), Instrument.FORTEPIANO);
    private WarmUpVoice voice2 = new WarmUpVoice(
            Arrays.asList(
                    new Note(new NoteRegister(NoteSymbol.F, -1), NoteValue.QUARTER),
                    new Rest(NoteValue.QUARTER),
                    new Note(new NoteRegister(NoteSymbol.D, 0), NoteValue.QUARTER),
                    new Note(new NoteRegister(NoteSymbol.C, 0), NoteValue.QUARTER)), Instrument.FORTEPIANO);
    private WarmUpVoice voice3 = new WarmUpVoice(
            Collections.singletonList(
                    (MusicalSymbol) new Note(
                            new NoteRegister(NoteSymbol.F, 0), NoteValue.WHOLE)), Instrument.FORTEPIANO);
    private WarmUpVoice voice4 = new WarmUpVoice(
            Arrays.asList(
                    new Note(new NoteRegister(NoteSymbol.F, 0), NoteValue.QUARTER),
                    new Rest(NoteValue.QUARTER),
                    new Note(new NoteRegister(NoteSymbol.D, 0), NoteValue.QUARTER),
                    new Rest(NoteValue.EIGHTH)), Instrument.FORTEPIANO);

    @Test
    public void testTonicStateMachineEvenStep() {
        SequenceConstructor.TonicStateMachine stateMachine = new SequenceConstructor.TonicStateMachine(30, 36, new FixedStep(2));
        Assert.assertEquals(30, stateMachine.getCurrentTonic());
        Assert.assertEquals(false, stateMachine.isFinished());
        Assert.assertEquals(32, stateMachine.getNextTonic());
        Assert.assertEquals(false, stateMachine.isFinished());
        Assert.assertEquals(34, stateMachine.getNextTonic());
        Assert.assertEquals(false, stateMachine.isFinished());
        Assert.assertEquals(36, stateMachine.getNextTonic());
        Assert.assertEquals(false, stateMachine.isFinished());
        Assert.assertEquals(34, stateMachine.getNextTonic());
        Assert.assertEquals(false, stateMachine.isFinished());
        Assert.assertEquals(32, stateMachine.getNextTonic());
        Assert.assertEquals(false, stateMachine.isFinished());
        Assert.assertEquals(30, stateMachine.getNextTonic());
        Assert.assertEquals(true, stateMachine.isFinished());
    }

    @Test
    public void testTonicStateMachineOddStep() {
        SequenceConstructor.TonicStateMachine stateMachine = new SequenceConstructor.TonicStateMachine(30, 36, new FixedStep(3));
        Assert.assertEquals(30, stateMachine.getCurrentTonic());
        Assert.assertEquals(false, stateMachine.isFinished());
        Assert.assertEquals(33, stateMachine.getNextTonic());
        Assert.assertEquals(false, stateMachine.isFinished());
        Assert.assertEquals(36, stateMachine.getNextTonic());
        Assert.assertEquals(false, stateMachine.isFinished());
        Assert.assertEquals(33, stateMachine.getNextTonic());
        Assert.assertEquals(false, stateMachine.isFinished());
        Assert.assertEquals(30, stateMachine.getNextTonic());
        Assert.assertEquals(true, stateMachine.isFinished());
    }

    @Test
    public void testLowestAndHighestNoteInVoice() {
        Assert.assertEquals(new NoteRegister(NoteSymbol.C, 0),
                SequenceConstructor.getLowestNoteInVoice(voice1));
        Assert.assertEquals(new NoteRegister(NoteSymbol.F, 0),
                SequenceConstructor.getHighestNoteInVoice(voice1));

        Assert.assertEquals(new NoteRegister(NoteSymbol.F, -1),
                SequenceConstructor.getLowestNoteInVoice(voice2));
        Assert.assertEquals(new NoteRegister(NoteSymbol.D, 0),
                SequenceConstructor.getHighestNoteInVoice(voice2));
    }

    @Test
    public void testLowestAndHighestNoteMidiInSequence() {
        Assert.assertEquals(7, SequenceConstructor.getLowestTonicInSequence(5, 7, 2));
        Assert.assertEquals(12, SequenceConstructor.getLowestTonicInSequence(14, 20, 2));
        Assert.assertEquals(10, SequenceConstructor.getLowestTonicInSequence(15, 20, 2));
        Assert.assertEquals(11, SequenceConstructor.getLowestTonicInSequence(15, 20, 3));
        Assert.assertEquals(12, SequenceConstructor.getLowestTonicInSequence(15, 20, 4));

        Assert.assertEquals(94, SequenceConstructor.getHighestTonicInSequence(96, 94, 2));
        Assert.assertEquals(88, SequenceConstructor.getHighestTonicInSequence(87, 80, 2));
        Assert.assertEquals(89, SequenceConstructor.getHighestTonicInSequence(87, 80, 3));
    }

    @Test
    public void testLastNoteDuration() {
        Assert.assertEquals(NoteValue.QUARTER, SequenceConstructor.getLastNoteDuration(voice1));
        Assert.assertEquals(NoteValue.WHOLE, SequenceConstructor.getLastNoteDuration(voice3));
        Assert.assertEquals(NoteValue.EIGHTH, SequenceConstructor.getLastNoteDuration(voice4));
    }


    @Test
    public void testAddNote() throws Exception {
        testAddNote(50, 0, 100, 0);
        testAddNote(20, 100, 100, 0);
        testAddNote(20, 100, 100, 1);
    }

    @Test
    public void testAddStep() throws InvalidMidiDataException {
        Track track = new Track();
        SequenceConstructor.MidiTrack midiTrack = SequenceConstructor.MidiTrack.MELODY;
        int channel = 0;
        int tonic = 50;
        long previousTick = 50;

        SequenceConstructor.addStepVoice(track, midiTrack, voice1, tonic, previousTick);

        Track trackExpected = new Track();
        long position = previousTick;
        trackExpected.add(new MidiTrackSpecificEvent(
                new MidiEvent(
                        new ShortMessage(ShortMessage.NOTE_ON, channel, 50 + 5, SequenceConstructor.VOLUME),
                        position), midiTrack.getIndex()));
        position += MidiUtils.getNoteValueInTicks(NoteValue.QUARTER);
        trackExpected.add(new MidiTrackSpecificEvent(
                new MidiEvent(
                        new ShortMessage(ShortMessage.NOTE_OFF, channel, 50 + 5, SequenceConstructor.VOLUME),
                        position), midiTrack.getIndex()));
        position += MidiUtils.getNoteValueInTicks(NoteValue.EIGHTH);
        trackExpected.add(new MidiTrackSpecificEvent(
                new MidiEvent(
                        new ShortMessage(ShortMessage.NOTE_ON, channel, 50 + 2, SequenceConstructor.VOLUME),
                        position), midiTrack.getIndex()));
        position += MidiUtils.getNoteValueInTicks(NoteValue.QUARTER);
        trackExpected.add(new MidiTrackSpecificEvent(
                new MidiEvent(
                        new ShortMessage(ShortMessage.NOTE_OFF, channel, 50 + 2, SequenceConstructor.VOLUME),
                        position), midiTrack.getIndex()));
        trackExpected.add(new MidiTrackSpecificEvent(
                new MidiEvent(
                        new ShortMessage(ShortMessage.NOTE_ON, channel, 50 + 0, SequenceConstructor.VOLUME),
                        position), midiTrack.getIndex()));
        position += MidiUtils.getNoteValueInTicks(NoteValue.QUARTER);
        trackExpected.add(new MidiTrackSpecificEvent(
                new MidiEvent(
                        new ShortMessage(ShortMessage.NOTE_OFF, channel, 50 + 0, SequenceConstructor.VOLUME),
                        position), midiTrack.getIndex()));

        Assert.assertEquals(trackExpected.size(), track.size());
        Assert.assertEquals(trackExpected.get(0), track.get(0));
        Assert.assertEquals(trackExpected.get(1), track.get(1));
        Assert.assertEquals(trackExpected.get(2), track.get(2));
        Assert.assertEquals(trackExpected.get(3), track.get(3));
        Assert.assertEquals(trackExpected.get(4), track.get(4));
        Assert.assertEquals(trackExpected.get(5), track.get(5));
    }


    private void testAddNote(int tonic, int position,long duration, int channel) throws InvalidMidiDataException {
        Track track = new Track();
        SequenceConstructor.MidiTrack midiTrack = SequenceConstructor.MidiTrack.MELODY;
        SequenceConstructor.addNote(track, midiTrack, channel, tonic, duration, position);
        Assert.assertEquals(2, track.size());
        MidiTrackSpecificEvent event1 =
                new MidiTrackSpecificEvent(
                        new MidiEvent(
                                new ShortMessage(ShortMessage.NOTE_ON, channel, tonic, SequenceConstructor.VOLUME),
                                position), midiTrack.getIndex());
        MidiTrackSpecificEvent event2 =
                new MidiTrackSpecificEvent(
                        new MidiEvent(
                                new ShortMessage(ShortMessage.NOTE_OFF, channel, tonic, SequenceConstructor.VOLUME),
                                position + duration), midiTrack.getIndex());
        Assert.assertEquals(track.get(0), event1);
        Assert.assertEquals(track.get(1), event2);
    }

    @Test
    public void testNoHarmonyOneDirection() throws Exception {
        WarmUp warmUp = new WarmUp();
        warmUp.setLowerNote(new NoteRegister(NoteSymbol.C, 0));
        warmUp.setUpperNote(new NoteRegister(NoteSymbol.C, 1));
        warmUp.setStartingNote(new NoteRegister(NoteSymbol.A, 0));
        warmUp.setDirections(new Direction[]{Direction.START_TO_UPPER});
        warmUp.setStep(new FixedStep(2));
        warmUp.setRunMelody(true);

        List<MusicalSymbol> musicalSymbols = Arrays.asList(new MusicalSymbol[]{
                new Note(NoteSymbol.C, 0, NoteValue.QUARTER),
                new Note(NoteSymbol.C_SHARP, 0, NoteValue.QUARTER),
                new Note(NoteSymbol.D, 0, NoteValue.QUARTER)
        });

        warmUp.setMelody(new Melody(new WarmUpVoice(musicalSymbols, Instrument.FORTEPIANO)));
        warmUp.setAdjustmentRules(SilentAdjustmentRules.valueOf(null));
        WarmUpSequence sequence = SequenceConstructor.construct(warmUp);
        System.out.println(sequence.getSequence().getTracks()[0].ticks());

//        int[] expectedMidiNotes = new int[]{36, 37, 38,
//                                            38, 39, 40,
//                                            40, 41, 42,
//                                            42, 43, 44,
//                                            44, 45, 46,
//                                            46, 47, 48,
//                                            48, 49, 50};
//
//        long[] expectedPositions = new long[]{0,     96,    96,    96*2,  96*2,  96*3,
//                                              96*3,  96*4,  96*4,  96*5,  96*5,  96*6,
//                                              96*6,  96*7,  96*7,  96*8,  96*8,  96*9,
//                                              96*9,  96*10, 96*10, 96*11, 96*11, 96*12,
//                                              96*12, 96*13, 96*13, 96*14, 96*14, 96*15,
//                                              96*15, 96*16, 96*16, 96*17, 96*17, 96*18,
//                                              96*18, 96*19, 96*19, 96*20, 96*20, 96*21};
//
//        Track[] tracks = sequence.getTracks();
//        Assert.assertEquals(4, tracks.length);
//        Assert.assertEquals(0, tracks[HARMONY.getIndex()].size());
//        Assert.assertEquals(0, tracks[LYRICS.getIndex()].size());
//        Assert.assertEquals(0, tracks[METRONOME.getIndex()].size());
//
//        for (int i = 0; i < tracks[MELODY.getIndex()].size(); i++) {
//            MidiEvent event = tracks[MELODY.getIndex()].get(i);
//            int midiNote = ((ShortMessage)event.getMessage()).getData1();
//            long midiPosition = event.getTick();
//            int expectedMidiNote = expectedMidiNotes[Math.round(i / 2)];
//            Assert.assertEquals(expectedMidiNote, midiNote);
//            Assert.assertEquals(expectedPositions[i], midiPosition);
//        }
    }

    // TODO: Marina and Mikhail add much more tests
}
