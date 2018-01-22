package com.berniac.vocalwarmup.sequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Mikhail Lipkovich on 11/17/2017.
 */
public class Harmony implements Playable {

    private List<WarmUpVoice> harmony;

    public Harmony(List<WarmUpVoice> harmony) {
        this.harmony = harmony;
    }

    public static Harmony valueOf(String str) {
        List<WarmUpVoice> harmony = new ArrayList<>();
        Scanner scanner = new Scanner(str);
        while (scanner.hasNext()) {
            String line = scanner.next();
            harmony.add(WarmUpVoice.valueOf(line));
        }
        scanner.close();
        //probably should be checked that all voices have the same length
        return new Harmony(harmony);
    }

    @Override
    public List<WarmUpVoice> getVoices() {
        return harmony;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Harmony)) return false;

        Harmony that = (Harmony) o;

        return harmony.equals(that.harmony);
    }

    @Override
    public String toString() {
        return "Harmony{" +
                "harmony=" + harmony +
                '}';
    }
}
