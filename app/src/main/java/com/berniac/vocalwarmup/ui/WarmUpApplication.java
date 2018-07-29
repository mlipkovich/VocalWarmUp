package com.berniac.vocalwarmup.ui;

import android.app.Application;

import com.berniac.vocalwarmup.midi.SF2Database;
import com.berniac.vocalwarmup.midi.SF2Sequencer;
import com.berniac.vocalwarmup.ui.model.RepositoryFactory;

import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by Mikhail Lipkovich on 2/18/2018.
 */
public class WarmUpApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SF2Sequencer.setSoundbank(ResourcesProvider.getSf2Database(this));
//        SF2Database.configure("Me:0,0\nFo:0,0");
        SF2Database.configure("Me:1\nFo:1");

        Reader hierarchyReader = new InputStreamReader(ResourcesProvider.getLibraryHierarchy(this));
        Reader presetsReader = new InputStreamReader(ResourcesProvider.getPresets(this));
        RepositoryFactory.construct(hierarchyReader, presetsReader);
    }
}
