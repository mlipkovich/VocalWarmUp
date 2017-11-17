package com.berniac.vocalwarmup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.berniac.vocalwarmup.midi.SF2Sequencer;
import com.berniac.vocalwarmup.music.MusicalSymbol;
import com.berniac.vocalwarmup.music.Note;
import com.berniac.vocalwarmup.music.NoteRegister;
import com.berniac.vocalwarmup.music.NoteSymbol;
import com.berniac.vocalwarmup.music.NoteValue;
import com.berniac.vocalwarmup.sequence.Direction;
import com.berniac.vocalwarmup.sequence.Instrument;
import com.berniac.vocalwarmup.sequence.Melody;
import com.berniac.vocalwarmup.sequence.SequenceConstructor;
import com.berniac.vocalwarmup.sequence.WarmUp;
import com.berniac.vocalwarmup.sequence.WarmUpVoice;

import jp.kshoji.javax.sound.midi.Sequence;
import jp.kshoji.javax.sound.midi.Sequencer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {

            SF2Sequencer.configure(getAssets(), "SmallTimGM6mb.sf2");
            Sequencer sequencer = SF2Sequencer.getSequencer();

            WarmUp warmUp = new WarmUp();
            warmUp.setLowerNote(new NoteRegister(NoteSymbol.C, 0));
            warmUp.setUpperNote(new NoteRegister(NoteSymbol.C, 1));
            warmUp.setStartingNote(new NoteRegister(NoteSymbol.C, 0));
            warmUp.setDirections(new Direction[]{Direction.START_TO_UPPER, Direction.UPPER_TO_START});
            warmUp.setRunMelody(true);

            MusicalSymbol[] musicalSymbols = new MusicalSymbol[]{
                    new Note(NoteSymbol.C, 0, NoteValue.QUARTER),
                    new Note(NoteSymbol.C_SHARP, 0, NoteValue.QUARTER),
                    new Note(NoteSymbol.D, 0, NoteValue.QUARTER)
            };

            warmUp.setMelody(new Melody(new WarmUpVoice(musicalSymbols, Instrument.FORTEPIANO)));
            Sequence sequence = SequenceConstructor.construct(warmUp);
            sequencer.setSequence(sequence);
            sequencer.open();

            sequencer.start();
            Thread.sleep(20000);
            sequencer.stop();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
