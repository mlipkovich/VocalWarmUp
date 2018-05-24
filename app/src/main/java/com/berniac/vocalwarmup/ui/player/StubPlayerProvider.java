package com.berniac.vocalwarmup.ui.player;

import android.content.Context;

import com.berniac.vocalwarmup.midi.SF2Sequencer;
import com.berniac.vocalwarmup.music.FixedStep;
import com.berniac.vocalwarmup.music.NoteRegister;
import com.berniac.vocalwarmup.music.NoteSymbol;
import com.berniac.vocalwarmup.sequence.Harmony;
import com.berniac.vocalwarmup.sequence.Melody;
import com.berniac.vocalwarmup.sequence.Player;
import com.berniac.vocalwarmup.sequence.SequenceConstructor;
import com.berniac.vocalwarmup.sequence.WarmUp;
import com.berniac.vocalwarmup.sequence.WarmUpPlayer;
import com.berniac.vocalwarmup.sequence.WarmUpSequence;
import com.berniac.vocalwarmup.sequence.adjustment.AdjustmentRules;
import com.berniac.vocalwarmup.sequence.adjustment.FullAdjustmentRules;
import com.berniac.vocalwarmup.sequence.adjustment.MinimalAdjustmentRules;
import com.berniac.vocalwarmup.sequence.adjustment.SilentAdjustmentRules;
import com.berniac.vocalwarmup.ui.ResourcesProvider;

import jp.kshoji.javax.sound.midi.Sequencer;

/**
 * Created by Mikhail Lipkovich on 2/16/2018.
 * Just a temporary class for providing player with concrete sequence
 */
public class StubPlayerProvider {
    private static volatile Player player;

    public static Player getPlayer(Context view) {
        Player localInstance = player;
        if (localInstance == null) {
            synchronized (StubPlayerProvider.class) {
                localInstance = player;
                if (localInstance == null) {
                    try {
                        localInstance = createPlayer(view);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    player = localInstance;
                }
            }
        }
        return localInstance;
    }

    private static Player createPlayer(Context view) throws Exception {
        WarmUp warmUp = new WarmUp();
        warmUp.setStep(new FixedStep(2));
//        warmUp.setMelody(Melody.valueOf("Me(4C,4D,4E,4D,2C)"));
        warmUp.setMelody(Melody.valueOf("Me(4C,4D,4E,4F,4G,4F,4E,4D,2C)"));
        warmUp.setHarmony(Harmony.valueOf("Fo(2E1,4E1,4G1,2F1,8G1,8F1,8E1,8D1,2E1){D-1,+1<Cis1,-1;D2,-2}" + "\n" +
                "Fo(25C1,4A,1H,2C1){D-1,+1<Hes,-1;Hes1,-2;E2,-3}" + "\n" +
                "Fo(25G,4F,25G,4A,2G){F-1,+1<Aes,-1;Aes1,-2;E2,-3}" + "\n" +
                "Fo(2C-1,2G-1,2D-1,2G-2,2C-1){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}"));

        AdjustmentRules minimalRules = MinimalAdjustmentRules.valueOf(
                "Min<C[Fo(4E1,4N,2E1){D-1,+1<Cis1,-1;D2,-2} Fo(4C1,4N,2C1){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(4G,4N,2G){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(4C-1,4N,2C-1){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]" + "\n" +
                        "Des[Fo(4E1,4N,2Aes1){D-1,+1<Cis1,-1;D2,-2} Fo(4C1,4N,2Des1){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(4G,4N,2F){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(4C-1,4N,2Des-1){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]" + "\n" +
                        "D[Fo(4E1,4N,2D1){D-1,+1<Cis1,-1;D2,-2} Fo(4C1,4N,2A){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(4G,4N,2Fis){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(4C-1,4N,2D-1){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]" + "\n" +
                        "Ees[Fo(4E1,4N,2Ees1){D-1,+1<Cis1,-1;D2,-2} Fo(4C1,4N,2Hes){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(4G,4N,2G){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(4C-1,4N,2Ees){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]" + "\n" +
                        "E[Fo(4E1,4N,2E1){D-1,+1<Cis1,-1;D2,-2} Fo(4C1,4N,2H){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(4G,4N,2Gis){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(4C-1,4N,2E-1){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]" + "\n" +
                        "F[Fo(4E1,4N,2C1){D-1,+1<Cis1,-1;D2,-2} Fo(4C1,4N,2A){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(4G,4N,2F){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(4C-1,4N,2F-1){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]" + "\n" +
                        "Fis[Fo(4E1,4N,2Fis1){D-1,+1<Cis1,-1;D2,-2} Fo(4C1,4N,2Cis1){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(4G,4N,2Ais){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(4C-1,4N,2Fis-1){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]" + "\n" +
                        "G[Fo(4E1,4N,2D1){D-1,+1<Cis1,-1;D2,-2} Fo(4C1,4N,2H){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(4G,4N,2G){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(4C-1,4N,2G-1){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]" + "\n" +
                        "Aes[Fo(4E1,4N,2Aes1){D-1,+1<Cis1,-1;D2,-2} Fo(4C1,4N,2C1){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(4G,4N,2Ees){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(4C-1,4N,2Aes-2){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]" + "\n" +
                        "A[Fo(4E1,4N,2E1){D-1,+1<Cis1,-1;D2,-2} Fo(4C1,4N,2Cis1){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(4G,4N,2A){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(4C-1,4N,2A-2){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]" + "\n" +
                        "Hes[Fo(4E1,4N,2F1){D-1,+1<Cis1,-1;D2,-2} Fo(4C1,4N,2D1){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(4G,4N,2F){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(4C-1,4N,2Hes-2){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]" + "\n" +
                        "H[Fo(4E1,4N,2Fis1){D-1,+1<Cis1,-1;D2,-2} Fo(4C1,4N,2H){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(4G,4N,2Dis){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(4C-1,4N,2H-2){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]>"
        );
//        System.out.println("Rules: " + rules.getAdjustmentRules(1));

        AdjustmentRules fullRules = FullAdjustmentRules.valueOf("Full8<" +
                "C[Fo(2G1,4Fis1,4Aes1,25G1,4F1){D-1,+1<Cis1,-1;D2,-2} Fo(4C1,4H,4Hes,4Des1,4C1,4H,2C){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(2E,4Des,4F,4E,4D,45E,8F){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(4C-1,4E-1,4Fis-1,4F-1,2G-1,2C-1){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]\n" +
                "Des[Fo(2G1,2Fis1,1Eis1){D-1,+1<Cis1,-1;D2,-2} Fo(4C1,4H,4Ais,4Gis,4Fis,4Gis,4Ais,4His){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(2E1,45Cis1,8His,25Cis1,8His,8Ais){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(4C-1,4E-1,4Fis-1,4Gis-1,2Cis-1,2His-2){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]\n" +
                "D[Fo(2G1,4Fis1,4E1,1Fis1){D-1,+1<Cis1,-1;D2,-2} Fo(2E1,4D1,4Cis1,1D1){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(4C1,4H,2A,2A,4D,4Fis){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(4C-1,4G-1,4A-2,4A-2,2D-1,8Cis-1,8H-2,4A-2){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]\n" +
                "Ees[Fo(45G1,8F1,2E1,4Ees1,4D1,2Ees1){D-1,+1<Cis1,-1;D2,-2} Fo(4E1,4D1,4H,4Aes,4G,4F,8G,8D1,8C1,8D1){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(4C1,4A,4Gis,4H,1Hes){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(4C-1,4D-1,4E-1,4Aes-1,4Hes-1,4Hes-2,2Ees-1){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]\n" +
                "E[Fo(25E1,4Dis1,25E1,8N,8A1){D-1,+1<Cis1,-1;D2,-2} Fo(2C1,15H){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(4G,4A,4Gis,4Fis,25Gis,8Gis,8Gis){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(4C-1,4A-2,2H-2,2E-1,45Dis-1,8F-1){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]\n" +
                "F[Fo(4E1,2D1,8C1,8Hes,1F1){D-1,+1<Cis1,-1;D2,-2} Fo(4C1,4A,4Hes,4G,2F,4A,4C1){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(4G,2F,4E,1A){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(4C-1,4D-1,4G-2,4C-1,1F-1){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]\n" +
                "Fis[Fo(2G1,45Fis1,8Eis1,25Cis1,8H,8Gis){D-1,+1<Cis1,-1;D2,-2} Fo(4E1,8D1,8H,4Ais,4Gis,1Fis){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(4C1,4D1,4Cis1,4H,1Ais){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(4C-1,4H-2,2Cis-1,2Fis-1,4Cis-1,4Ais-2){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]\n" +
                "G[Fo(2E1,15D1){D-1,+1<Cis1,-1;D2,-2} Fo(4C1,4A,4G,4Fis,1G){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(4G,4C1,4H,4A,2H,8A,8G,8Fis,8E){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(2C-1,2D-1,2G-1,4E-1,4Fis-1){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]\n" +
                "Aes[Fo(45G1,8F1,4E1,4Cis1,2Hes,4C1,4G1){D-1,+1<Cis1,-1;D2,-2} Fo(4E1,4D1,4Cis1,4A,2G,2Aes){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(4C1,2A,4E,1Ees){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(4C-1,4D-1,4A-2,4Des-1,2Ees-1,2Aes-2){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]\n" +
                "A[Fo(4G1,4F1,1E1,4Gis1,8Fis1,8E1){D-1,+1<Cis1,-1;D2,-2} Fo(4E1,8D1,8H,4A,4Gis){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(8C1,8H,8A,8D1,4Cis1,4H,45Cis1,8Cis1,8H,8Gis,8Fis,8D){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(4C-1,4D-1,2E-1,2A-2,4Cis-1,4E-1){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]\n" +
                "Hes[Fo(2E1,4Ees1,4Ges1,1F1){D-1,+1<Cis1,-1;D2,-2} Fo(2C1,2H,4Hes,4A,2Hes){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(4G,4A,4Ges,4Ees1,4D1,4C1,4D1,16C1,16Hes,16A,16G){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(4C-1,4A-2,4H-2,4Ees-1,2F-1,2Hes-1){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]\n" +
                "H[Fo(2G1,1Fis1,8G,8E,8Cis,8E){D-1,+1<Cis1,-1;D2,-2} Fo(4C1,2H,4Ais,1H){D-1,+1<Hes,-1;Hes1,-2;E2,-3} Fo(2E,4Dis,4Cis,1Dis){F-1,+1<Aes,-1;Aes1,-2;E2,-3} Fo(4C-1,4E-1,2Fis-1,2H-2,2Fis-2){Hes-1,+1;C-1,+2<Cis1,-1;Cis2,-2;A2,-3}]" +
                ">");

        System.out.println("Full rules " + fullRules.getAdjustmentRules(8));
        warmUp.setAdjustmentRules(SilentAdjustmentRules.valueOf(null));
//        warmUp.setAdjustmentRules(minimalRules);
//        warmUp.setAdjustmentRules(fullRules);
        System.out.println("Adjustments set");
        warmUp.setLowerNote(new NoteRegister(NoteSymbol.H, -1));
        warmUp.setUpperNote(new NoteRegister(NoteSymbol.H, 0));
        warmUp.setStartingNote(new NoteRegister(NoteSymbol.H, -1));
        warmUp.setPauseSize(8);

        WarmUpSequence warmUpSequence = SequenceConstructor.construct(warmUp);
        System.out.println("Sequence constructed");
        Sequencer sequencer = SF2Sequencer.getSequencer();
        return new WarmUpPlayer(warmUpSequence, sequencer, warmUp);
    }
}
