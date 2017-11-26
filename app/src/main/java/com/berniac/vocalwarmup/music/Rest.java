package com.berniac.vocalwarmup.music;

/**
 * Created by Mikhail Lipkovich on 11/17/2017.
 * Stores musical rest (silence)
 */
public class Rest implements MusicalSymbol {

    private NoteValue noteValue;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rest)) return false;

        Rest rest = (Rest) o;

        return noteValue == rest.noteValue;
    }

    public Rest(NoteValue noteValue) {
        this.noteValue = noteValue;
    }

    @Override
    public boolean isSounding() {
        return false;
    }

    @Override
    public NoteValue getNoteValue() {
        return noteValue;
    }
}
