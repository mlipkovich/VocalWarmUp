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

    Harmony(List<WarmUpVoice> harmony) {
        this.harmony = harmony;
    }

    public static Harmony valueOf(String str) {
        List<WarmUpVoice> harmony = new ArrayList<>();
        Scanner scanner = new Scanner(str);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
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

        if (!harmony.equals(that.harmony)) return false;
        return true;
    }
}
