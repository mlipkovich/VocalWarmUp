package com.berniac.vocalwarmup.ui.player;

import com.berniac.vocalwarmup.midi.SF2Sequencer;
import com.berniac.vocalwarmup.model.Preset;
import com.berniac.vocalwarmup.music.FixedStep;
import com.berniac.vocalwarmup.music.NoteRegister;
import com.berniac.vocalwarmup.sequence.Accompaniment;
import com.berniac.vocalwarmup.sequence.Melody;
import com.berniac.vocalwarmup.sequence.Player;
import com.berniac.vocalwarmup.sequence.SequenceFinishedListener;
import com.berniac.vocalwarmup.sequence.WarmUp;
import com.berniac.vocalwarmup.sequence.WarmUpPlayer;
import com.berniac.vocalwarmup.sequence.sequencer.QueueStepConsumer;
import com.berniac.vocalwarmup.sequence.sequencer.QueueStepProducer;
import com.berniac.vocalwarmup.sequence.sequencer.StepConsumer;
import com.berniac.vocalwarmup.sequence.sequencer.StepProducer;
import com.berniac.vocalwarmup.sequence.sequencer.StepSequencer;
import com.berniac.vocalwarmup.sequence.sequencer.WarmUpStep;
import com.berniac.vocalwarmup.ui.model.IWarmUpRepository;
import com.berniac.vocalwarmup.ui.model.RepositoryFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import jp.kshoji.javax.sound.midi.Receiver;

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

    public void onAttach(final PlayerView view) {
        this.view = view;
        IWarmUpRepository repository = RepositoryFactory.getRepository();
        // TODO: Work with regular draws as well
        Preset presetToPlay = (Preset)repository.getSelectedItem();

        WarmUp warmUp = new WarmUp();
        warmUp.setStep(new FixedStep(presetToPlay.getStep()));
        warmUp.setMelody(Melody.valueOf(presetToPlay.getMelody()));
        warmUp.setAccompaniment(Accompaniment.valueOf(presetToPlay.getAccompaniment()));

        warmUp.setLowerNote(NoteRegister.valueOf(presetToPlay.getLowerNote()));
        warmUp.setStartingNote(NoteRegister.valueOf(presetToPlay.getStartingNote()));
        warmUp.setUpperNote(NoteRegister.valueOf(presetToPlay.getUpperNote()));
        warmUp.setPauseSize(presetToPlay.getPauseSize());
        warmUp.setDirections(presetToPlay.getDirections());

        Receiver receiver = SF2Sequencer. getReceiver();

        // TODO: Move to some kind of factory
        BlockingQueue<WarmUpStep> queue = new ArrayBlockingQueue<>(10);
        StepConsumer stepConsumer = new QueueStepConsumer(queue);
        StepProducer stepProducer = new QueueStepProducer(queue, warmUp);
        StepSequencer  stepSequencer = new StepSequencer(stepConsumer, stepProducer, receiver);

        this.player = new WarmUpPlayer(stepSequencer, new SequenceFinishedListener() {
            @Override
            public void onSequenceFinished() {
                if (isPlaying) {
                    view.changePlayButtonToPlay();
                    isPlaying = false;
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

    public void onNavigateUp() {
        if (isPlaying) {
            player.pause();
        }
    }
}
