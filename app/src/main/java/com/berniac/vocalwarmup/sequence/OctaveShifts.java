package com.berniac.vocalwarmup.sequence;

import com.berniac.vocalwarmup.music.NoteRegister;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mikhail Lipkovich on 2/10/2018.
 */
public class OctaveShifts {

    public static final OctaveShifts EMPTY_SHIFTS = new OctaveShifts(
            Collections.<BoundaryNote>emptyList(), Collections.<BoundaryNote>emptyList());

    private List<BoundaryNote> lowerBoundaries;
    private List<BoundaryNote> upperBoundaries;

    OctaveShifts(List<BoundaryNote> lowerBoundaries, List<BoundaryNote> upperBoundaries) {
        this.lowerBoundaries = lowerBoundaries;
        this.upperBoundaries = upperBoundaries;
    }

    public static OctaveShifts valueOf(String str) {
        int shiftsStart = str.indexOf('{');
        if (str.equals("") || shiftsStart == -1) {
            return EMPTY_SHIFTS;
        }
        int shiftsEnd = str.indexOf('}');
        int splitIndex = str.indexOf("<");
        String lowerShifts = str.substring(shiftsStart + 1, splitIndex);
        List<BoundaryNote> lowerBoundaries = getBoundaries(lowerShifts);
        String upperShifts = str.substring(splitIndex + 1, shiftsEnd);
        List<BoundaryNote> upperBoundaries = getBoundaries(upperShifts);
        return new OctaveShifts(lowerBoundaries, upperBoundaries);
    }

    public static OctaveShifts valueOfNew(String str) {
        int shiftsStart = str.indexOf('[');
        if (str.equals("") || shiftsStart == -1) {
            return EMPTY_SHIFTS;
        }
        int shiftsEnd = str.indexOf(']');
        int splitIndex = str.indexOf("<");
        String lowerShifts = str.substring(shiftsStart + 1, splitIndex);
        List<BoundaryNote> lowerBoundaries = getBoundaries(lowerShifts);
        String upperShifts = str.substring(splitIndex + 1, shiftsEnd);
        List<BoundaryNote> upperBoundaries = getBoundaries(upperShifts);
        return new OctaveShifts(lowerBoundaries, upperBoundaries);
    }

    public List<BoundaryNote> getLowerBoundaries() {
        return lowerBoundaries;
    }

    public List<BoundaryNote> getUpperBoundaries() {
        return upperBoundaries;
    }

    private static List<BoundaryNote> getBoundaries(String str) {
        String[] boundaries = str.split(";");
        List<BoundaryNote> boundaryNotes = new ArrayList<>(boundaries.length);
        for (String boundary : boundaries) {
            String[] splitted = boundary.split(",");
            NoteRegister note = NoteRegister.valueOf(splitted[0]);
            int shift = Integer.valueOf(splitted[1]);
            boundaryNotes.add(new BoundaryNote(note, shift));
        }
        return boundaryNotes;
    }

    public static class BoundaryNote {
        private NoteRegister boundary;
        private int shift;

        public BoundaryNote(NoteRegister boundary, int shift) {
            this.boundary = boundary;
            this.shift = shift;
        }

        public NoteRegister getBoundary() {
            return boundary;
        }

        public int getShift() {
            return shift;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BoundaryNote that = (BoundaryNote) o;

            if (shift != that.shift) return false;
            return boundary.equals(that.boundary);

        }

        @Override
        public int hashCode() {
            int result = boundary.hashCode();
            result = 31 * result + shift;
            return result;
        }

        @Override
        public String toString() {
            return "BoundaryNote{" +
                    "boundary=" + boundary +
                    ", shift=" + shift +
                    '}';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OctaveShifts shifts = (OctaveShifts) o;

        if (!lowerBoundaries.equals(shifts.lowerBoundaries)) return false;
        return upperBoundaries.equals(shifts.upperBoundaries);

    }

    @Override
    public int hashCode() {
        int result = lowerBoundaries.hashCode();
        result = 31 * result + upperBoundaries.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "OctaveShifts{" +
                "lowerBoundaries=" + lowerBoundaries +
                ", upperBoundaries=" + upperBoundaries +
                '}';
    }
}
