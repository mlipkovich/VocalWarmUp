package com.berniac.vocalwarmup.ui.player;

import com.berniac.vocalwarmup.midi.SF2Sequencer;
import com.berniac.vocalwarmup.music.FixedStep;
import com.berniac.vocalwarmup.music.NoteRegister;
import com.berniac.vocalwarmup.music.NoteSymbol;
import com.berniac.vocalwarmup.sequence.Melody;
import com.berniac.vocalwarmup.sequence.Player;
import com.berniac.vocalwarmup.sequence.SequenceConstructor;
import com.berniac.vocalwarmup.sequence.WarmUp;
import com.berniac.vocalwarmup.sequence.WarmUpPlayer;
import com.berniac.vocalwarmup.sequence.WarmUpSequence;
import com.berniac.vocalwarmup.sequence.adjustment.SilentAdjustmentRules;
import com.berniac.vocalwarmup.ui.ResourcesProvider;

import jp.kshoji.javax.sound.midi.Sequencer;

/**
 * Created by Mikhail Lipkovich on 1/23/2018.
 */
public class PlayerPresenter {

    private PlayerView view;
    private boolean isPlaying = false;
    private boolean isHarmonySwitchedOff = false;
    private boolean isMelodySwitchedOff = false;
    private Player player;

    public PlayerPresenter() {
    }

    public void onAttach(PlayerView view) {
        // TODO: Use particular presets here, make sure create sequence before the screen starts
        this.view = view;
        WarmUp warmUp = new WarmUp();
        warmUp.setStep(new FixedStep(2));
        warmUp.setMelody(Melody.valueOf("Me(4C,4D,4E,4D,2C)"));
        warmUp.setLowerNote(new NoteRegister(NoteSymbol.C, 0));
        warmUp.setUpperNote(new NoteRegister(NoteSymbol.C, 2));
        warmUp.setStartingNote(new NoteRegister(NoteSymbol.C, 1));
        warmUp.setPauseSize(4);
        warmUp.setAdjustmentRules(SilentAdjustmentRules.valueOf(null));
        try {
            WarmUpSequence warmUpSequence = SequenceConstructor.construct(warmUp);
            SF2Sequencer.configure(ResourcesProvider.getSf2Database(view));
            Sequencer sequencer = SF2Sequencer.getSequencer();
            player = new WarmUpPlayer(warmUpSequence.getSequence(), sequencer, warmUp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onTempoChanged(int progress) {
        view.changeTempoProgress(progress);
        player.changeTemp();
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

    public void onHarmonySwitcherClicked() {
        if (isHarmonySwitchedOff) {
            view.changeHarmonyButtonToOn();
            player.harmonyOn();
        } else {
            view.changeHarmonyButtonToOff();
            player.harmonyOff();
        }
        isHarmonySwitchedOff = !isHarmonySwitchedOff;
    }

    public void onMelodySwitcherClicked() {
        if (isMelodySwitchedOff) {
            view.changeMelodyButtonToOn();
            player.melodyOn();
        } else {
            view.changeMelodyButtonToOff();
            player.melodyOff();
        }
        isMelodySwitchedOff = !isMelodySwitchedOff;
    }
}
