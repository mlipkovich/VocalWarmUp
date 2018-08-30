package com.berniac.vocalwarmup.ui;

import android.app.Application;

import com.berniac.vocalwarmup.midi.SF2Database;
import com.berniac.vocalwarmup.midi.SF2Synthesizer;
import com.berniac.vocalwarmup.sequence.Instrument;
import com.berniac.vocalwarmup.ui.model.RepositoryFactory;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import cn.sherlock.com.sun.media.sound.SF2Soundbank;

/**
 * Created by Mikhail Lipkovich on 2/18/2018.
 */
public class WarmUpApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SF2Soundbank soundbank = SF2Database.readSoundbank(ResourcesProvider.getSf2Database(this));
        Map<Instrument, SF2Database.Program> description =  SF2Database.parseDescription("Me:1\nFo:1\nMm:36");
        SF2Synthesizer.configure(soundbank, description);

        Reader hierarchyReader = new InputStreamReader(ResourcesProvider.getLibraryHierarchy(this));
        Reader presetsReader = new InputStreamReader(ResourcesProvider.getPresets(this));
        RepositoryFactory.construct(hierarchyReader, presetsReader);
    }
}

