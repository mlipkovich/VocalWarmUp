package com.berniac.vocalwarmup.sequence;

import jp.kshoji.javax.sound.midi.Sequence;

/**
 * Created by Marina Gorlova on 02.12.2017.
 */
public interface Player {
    void play();

    void pause();

    void stop();

    void openSettings();

    void openConstructor();

    void repeatCurrentStep();

    void previousStep();

    void nextStep();

    void melodyOn();

    void melodyOff();

    void harmonyOn();

    void harmonyOff();

    void recordOn();

    void recordOff();

    void changeTemp();

    void showNoteVisualisation();

    void showLyrics();

    Sequence getSequence();
}
