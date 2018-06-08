package com.berniac.vocalwarmup.ui.player;

import com.berniac.vocalwarmup.sequence.Player;

/**
 * Created by Mikhail Lipkovich on 1/23/2018.
 */
public class PlayerPresenter {

    private PlayerView view;
    private PlayerScreenFragment screenView;
    private PlayerConfigFragment configView;

    private boolean isPlaying = false;
    private Player player;

    private boolean isHarmonySwitchedOff = false;
    private boolean isMelodySwitchedOff = false;

    public PlayerPresenter() {
    }

    public void onAttach(PlayerView view) {
        this.view = view;
        this.player = StubPlayerProvider.getPlayer(view);
    }

    public void onAttachScreenFragment(PlayerScreenFragment screenView) {
        this.screenView = screenView;
    }

    public void onAttachConfigFragment(PlayerConfigFragment configView) {
        this.configView = configView;
    }

    public void onPlayClicked() {
        if (isPlaying) {
            view.changePlayButtonToPlay();
            player.pause();
        } else {
            view.changePlayButtonToPause();
            player.play();
        }
        isPlaying = !isPlaying;
    }

    public void onNextClicked() {
        player.nextStep();
    }

    public void onPreviousClicked() {
        player.previousStep();
    }

    public void onRepeatClicked() {
        player.repeatCurrentStep();
    }

    public void onRevertClicked() {
        view.changeDirection();
        player.changeDirection();
    }

    public void onTempoFactorChanged(int progress) {
        float tempoFactor = screenView.changeTempoProgress(progress);
        player.changeTempoFactor(tempoFactor);
    }

    public void onGlobalTempoChanged(int progress) {
        int tempoBpm = configView.changeGlobalTempoProgress(progress);
        player.changeTempo(tempoBpm);
    }

    public void onHarmonySwitcherClicked() {
        if (isHarmonySwitchedOff) {
            screenView.changeHarmonyButtonToOn();
            player.harmonyOn();
        } else {
            screenView.changeHarmonyButtonToOff();
            player.harmonyOff();
        }
        isHarmonySwitchedOff = !isHarmonySwitchedOff;
    }

    public void onMelodySwitcherClicked() {
        if (isMelodySwitchedOff) {
            screenView.changeMelodyButtonToOn();
            player.melodyOn();
        } else {
            screenView.changeMelodyButtonToOff();
            player.melodyOff();
        }
        isMelodySwitchedOff = !isMelodySwitchedOff;
    }

    public void onConfigPanelClicked() {
        view.switchToConfigPanel();
    }

    public void onScreenPanelClicked() {
        view.switchToScreenPanel();
    }
}
