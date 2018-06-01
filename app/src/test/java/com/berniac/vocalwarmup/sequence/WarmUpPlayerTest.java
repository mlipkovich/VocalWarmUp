package com.berniac.vocalwarmup.sequence;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Marina Gorlova on 23.01.2018.
 */
public class WarmUpPlayerTest {

    @Test
    public void testPlay() throws Exception {
        System.out.println(5%4);
        System.out.println(-5%4);
    }

    @Test
    public void testStartTickPosition() {
        Assert.assertEquals(
                2000,
                WarmUpPlayer.getDirectionStartPosition(10, 20, 5, 1000));
    }
}
