package com.berniac.vocalwarmup.ui;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mikhail Lipkovich on 12/1/2017.
 */
public class ResourcesProvider {

    private static final String HIERARCHY_ASSET = "draw_hierarchy.json";
    private static final String SF2_DATABASE_ASSET = "SmallTimGM6mb.sf2";
    private static final String DRAWABLE_DIR = "drawable";

    private ResourcesProvider() {}

    public static int getDrawable(Context context, String drawableName) {
        return context.getResources().getIdentifier(drawableName, DRAWABLE_DIR, context.getPackageName());
    }

    public static InputStream getDrawHierarchy(Context context) {
        try {
            return context.getAssets().open(HIERARCHY_ASSET);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get assets from " + HIERARCHY_ASSET);
        }
    }

    public static InputStream getSf2Database(Context context) {
        try {
            return context.getAssets().open(SF2_DATABASE_ASSET);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get assets from " + SF2_DATABASE_ASSET);
        }
    }
}
