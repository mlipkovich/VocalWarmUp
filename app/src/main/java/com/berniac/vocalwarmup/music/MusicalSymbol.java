package com.berniac.vocalwarmup.music;

/**
 * Created by Mikhail Lipkovich on 11/17/2017.
 * Common interface for possible symbol notations
 */
public interface MusicalSymbol {

    /**
     * @return whether symbol is related to entity which produces sound
     */
    boolean isSounding();

    /**
     * @return duration of a symbol
     */
    NoteValue getNoteValue();
}
