package com.berniac.vocalwarmup.ui.player;

import com.berniac.vocalwarmup.model.Preset;
import com.berniac.vocalwarmup.music.FixedStep;
import com.berniac.vocalwarmup.music.NoteRegister;
import com.berniac.vocalwarmup.sequence.Accompaniment;
import com.berniac.vocalwarmup.sequence.Direction;
import com.berniac.vocalwarmup.sequence.DirectionChangedListener;
import com.berniac.vocalwarmup.sequence.Melody;
import com.berniac.vocalwarmup.sequence.Player;
import com.berniac.vocalwarmup.sequence.SequenceFinishedListener;
import com.berniac.vocalwarmup.sequence.WarmUp;
import com.berniac.vocalwarmup.sequence.WarmUpPlayer;
import com.berniac.vocalwarmup.sequence.sequencer.StepSequencer;
import com.berniac.vocalwarmup.ui.model.IWarmUpRepository;
import com.berniac.vocalwarmup.ui.model.RepositoryFactory;


/**
 * Created by Mikhail Lipkovich on 1/23/2018.
 */
public class PlayerPresenter {

    private PlayerView view;
    private String drawTitle;
    private PlayerScreenFragment screenView;
    private PlayerConfigFragment configView;

    private volatile boolean isPlaying = false;
    private volatile boolean isDirectionForward = true;

    private Player player;

    private boolean isHarmonySwitchedOff = false;
    private boolean isMelodySwitchedOff = false;
    private boolean isAdjustmentSwitchedOff = false;
    private boolean isMetronomeSwitchedOff = true;
    private int melodyVolumeBeforeMute = 100;
    private int harmonyVolumeBeforeMute = 100;
    private int adjustmentVolumeBeforeMute = 100;
    private int metronomeVolumeUnMute = 127;

    public PlayerPresenter() {
    }

    public void onAttach(final PlayerView view) {
        this.view = view;
        IWarmUpRepository repository = RepositoryFactory.getRepository();
        // TODO: Work with regular draws as well
        Preset presetToPlay = (Preset)repository.getSelectedItem();
        this.drawTitle = presetToPlay.getName();

        WarmUp warmUp = new WarmUp();
        warmUp.setStep(new FixedStep(presetToPlay.getStep()));
        warmUp.setMelody(Melody.valueOf(presetToPlay.getMelody()));
        warmUp.setAccompaniment(Accompaniment.valueOf(presetToPlay.getAccompaniment()));

        warmUp.setLowerNote(NoteRegister.valueOf(presetToPlay.getLowerNote()));
        warmUp.setStartingNote(NoteRegister.valueOf(presetToPlay.getStartingNote()));
        warmUp.setUpperNote(NoteRegister.valueOf(presetToPlay.getUpperNote()));
        warmUp.setPauseSize(presetToPlay.getPauseSize());
        warmUp.setDirections(presetToPlay.getDirections());

        StepSequencer stepSequencer = new StepSequencer(warmUp);
        this.player = new WarmUpPlayer(stepSequencer,
                new SequenceFinishedListener() {
                    @Override
                    public void onSequenceFinished() {
                        if (isPlaying) {
                            view.changePlayButtonToPlay();
                            isPlaying = false;
                        }
                    }
                },
                new DirectionChangedListener() {
                    @Override
                    public void onDirectionChanged(Direction newDirection) {
                        switch (newDirection) {
                            case LOWER_TO_START:
                            case LOWER_TO_UPPER:
                            case START_TO_UPPER:
                                view.changeDirectionButtonToRight();
                                isDirectionForward = true;
                                break;
                            case UPPER_TO_START:
                            case START_TO_LOWER:
                            case UPPER_TO_LOWER:
                                view.changeDirectionButtonToLeft();
                                isDirectionForward = false;
                                break;
                        }
                    }
                });
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
            isPlaying = false;
        } else {
            view.changePlayButtonToPause();
            player.play();
            isPlaying = true;
        }
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
        if (isDirectionForward) {
            isDirectionForward = false;
            view.changeDirectionButtonToLeft();
        } else {
            isDirectionForward = true;
            view.changeDirectionButtonToRight();
        }
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

    public void onMelodySwitcherClicked() {
        if (isMelodySwitchedOff) {
            configView.changeMelodyVolumeBar(melodyVolumeBeforeMute);
        } else {
            melodyVolumeBeforeMute = configView.changeMelodyVolumeBar(0);
        }
    }

    public void onMelodyVolumeChanged(int progress) {
        player.changeMelodyVolume(progress);
        if (progress == 0) {
            screenView.changeMelodyButtonToOff();
            configView.changeMelodyButtonToOff();
            isMelodySwitchedOff = true;
        } else {
            screenView.changeMelodyButtonToOn();
            configView.changeMelodyButtonToOn();
            isMelodySwitchedOff = false;
        }
    }

    public void onHarmonySwitcherClicked() {
        if (isHarmonySwitchedOff) {
            configView.changeHarmonyVolumeBar(harmonyVolumeBeforeMute);
        } else {
            harmonyVolumeBeforeMute = configView.changeHarmonyVolumeBar(0);
        }
    }

    public void onHarmonyVolumeChanged(int progress) {
        player.changeHarmonyVolume(progress);
        if (progress == 0) {
            screenView.changeHarmonyButtonToOff();
            configView.changeHarmonyButtonToOff();
            isHarmonySwitchedOff = true;
        } else {
            screenView.changeHarmonyButtonToOn();
            configView.changeHarmonyButtonToOn();
            isHarmonySwitchedOff = false;
        }
    }

    public void onAdjustmentSwitcherClicked() {
        if (isAdjustmentSwitchedOff) {
            configView.changeAdjustmentVolumeBar(adjustmentVolumeBeforeMute);
        } else {
            adjustmentVolumeBeforeMute = configView.changeAdjustmentVolumeBar(0);
        }
    }

    public void onAdjustmentVolumeChanged(int progress) {
        player.changeAdjustmentVolume(progress);
        if (progress == 0) {
            configView.changeAdjustmentButtonToOff();
            isAdjustmentSwitchedOff = true;
        } else {
            configView.changeAdjustmentButtonToOn();
            isAdjustmentSwitchedOff = false;
        }
    }

    public void onMetronomeSwitcherClicked() {
        if (isMetronomeSwitchedOff) {
            player.changeMetronomeVolume(metronomeVolumeUnMute);
            configView.changeMetronomeButtonToOn();
        } else {
            player.changeMetronomeVolume(0);
            configView.changeMetronomeButtonToOff();
        }
        isMetronomeSwitchedOff = !isMetronomeSwitchedOff;
    }

    public void onConfigPanelClicked() {
        view.switchToConfigPanel();
    }

    public void onScreenPanelClicked() {
        view.switchToScreenPanel();
    }

    public void onNavigateUp() {
        player.stop();
    }

    public void onDrawTitleInitialized() {
        screenView.setDrawTitle(drawTitle);
    }
}
