package com.berniac.vocalwarmup.sequence;

import com.berniac.vocalwarmup.music.NoteRegister;
import com.berniac.vocalwarmup.music.NoteSymbol;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mikhail Lipkovich on 2/10/2018.
 */
public class OctaveShiftsTest {
    @Test
    public void testValueOf() {
        Assert.assertEquals(new OctaveShifts(new ArrayList<OctaveShifts.BoundaryNote>(),
                new ArrayList<OctaveShifts.BoundaryNote>()), OctaveShifts.valueOf(""));

        List<OctaveShifts.BoundaryNote> lowerBoundaries = Collections.singletonList(
                new OctaveShifts.BoundaryNote(new NoteRegister(NoteSymbol.D, -1), 1));
        List<OctaveShifts.BoundaryNote> upperBoundaries = Arrays.asList(
                new OctaveShifts.BoundaryNote(new NoteRegister(NoteSymbol.C_SHARP, 1), -1),
                new OctaveShifts.BoundaryNote(new NoteRegister(NoteSymbol.D, 2), -2));
        Assert.assertEquals(new OctaveShifts(lowerBoundaries, upperBoundaries),
                OctaveShifts.valueOf("{D-1,+1<Cis1,-1;D2,-2}"));
    }
}
