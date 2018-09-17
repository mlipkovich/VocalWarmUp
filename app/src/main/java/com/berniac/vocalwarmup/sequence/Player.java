package com.berniac.vocalwarmup.sequence;

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

    void changeDirection();

    void previousStep();

    void nextStep();

    void recordOn();

    void recordOff();

    void changeTempoFactor(float tempoFactor);

    void changeTempo(int tempoBpm);

    void changeMelodyVolume(int volume);

    void changeHarmonyVolume(int volume);

    void changeAdjustmentVolume(int volume);

    void changeMetronomeVolume(int volume);

    void showNoteVisualisation();

    void showLyrics();
}
