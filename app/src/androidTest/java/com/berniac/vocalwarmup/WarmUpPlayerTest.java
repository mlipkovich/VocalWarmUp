package com.berniac.vocalwarmup;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

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

import org.junit.Test;
import org.junit.runner.RunWith;

import jp.kshoji.javax.sound.midi.Sequence;
import jp.kshoji.javax.sound.midi.Sequencer;

import static org.junit.Assert.assertEquals;

/**
 * Created by Marina Gorlova on 02.12.2017.
 */
@RunWith(AndroidJUnit4.class)
public class WarmUpPlayerTest {

    public WarmUpPlayerTest(){
    }
    @Test
    public void playTest() throws Exception {



        assertEquals("com.berniac.vocalwarmup", appContext.getPackageName());
    }
}
