package com.berniac.vocalwarmup.ui.model;

import java.io.Reader;

/**
 * Created by Mikhail Lipkovich on 6/10/2018.
 */
public class RepositoryFactory {

    private static IWarmUpRepository repository;

    public static void construct(Reader hierarchyReader, Reader presetsReader) {
        repository = new WarmUpRepository(hierarchyReader, presetsReader);
    }

    public static IWarmUpRepository getRepository() {
        return repository;
    }
}
