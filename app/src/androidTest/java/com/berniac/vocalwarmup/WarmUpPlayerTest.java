package com.berniac.vocalwarmup;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by Marina Gorlova on 02.12.2017.
 */
@RunWith(AndroidJUnit4.class)
public class WarmUpPlayerTest {

    public WarmUpPlayerTest(){
    }
    @Test
    public void playTest() throws Exception {



        assertEquals("com.berniac.vocalwarmup", appContext.getPackageName());
    }
}
