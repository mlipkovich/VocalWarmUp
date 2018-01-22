package com.berniac.vocalwarmup.music;

import java.util.Random;

/**
 * Created by Mikhail Lipkovich on 1/19/2018.
 */
class RandomStep implements Step {

    private Random random = new Random();

    @Override
    public int getNextShift() {
        return random.nextInt(255);
    }
}
