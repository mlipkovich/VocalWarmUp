package com.berniac.vocalwarmup.music;

/**
 * Created by Mikhail Lipkovich on 11/17/2017.
 * Stores notes with their pitch and duration
 */
public class Note implements MusicalSymbol {

    private NoteRegister noteRegister;
    private NoteValue noteValue;

    public Note(NoteSymbol noteSymbol, int octave, NoteValue noteValue) {
        this.noteRegister = new NoteRegister(noteSymbol, octave);
        this.noteValue = noteValue;
    }

    public Note(NoteRegister noteRegister, NoteValue noteValue) {
        this.noteRegister = noteRegister;
        this.noteValue = noteValue;
    }

    @Override
    public boolean isSounding() {
        return true;
    }

    @Override
    public NoteValue getNoteValue() {
        return noteValue;
    }

    public NoteRegister getNoteRegister() {
        return noteRegister;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;

        Note note = (Note) o;

        if (noteRegister != null ?
                !noteRegister.equals(note.noteRegister) :
                note.noteRegister != null) {
            return false;
        }
        return noteValue == note.noteValue;
    }
}
