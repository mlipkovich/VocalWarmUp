package com.berniac.vocalwarmup.ui;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mikhail Lipkovich on 12/1/2017.
 */
public class ResourcesProvider {

    private static final String LIBRARY_ASSET = "draw_hierarchy.json";
    private static final String PRESETS_ASSET = "draw_presets.json";
    //private static final String SF2_DATABASE_ASSET = "SmallTimGM6mb.sf2";
    private static final String SF2_DATABASE_ASSET = "VocalWarmUpBank.sf2";
    private static final String DRAWABLE_DIR = "drawable";

    private ResourcesProvider() {}

    public static int getDrawable(Context context, String drawableName) {
        return context.getResources().getIdentifier(drawableName, DRAWABLE_DIR, context.getPackageName());
    }

    public static InputStream getLibraryHierarchy(Context context) {
        return getResource(context, LIBRARY_ASSET);
    }

    public static InputStream getPresets(Context context) {
        return getResource(context, PRESETS_ASSET);
    }

    public static InputStream getSf2Database(Context context) {
        return getResource(context, SF2_DATABASE_ASSET);
    }

    private static InputStream getResource(Context context, String name) {
        try {
            return context.getAssets().open(name);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get assets from " + name);
        }
    }
}
