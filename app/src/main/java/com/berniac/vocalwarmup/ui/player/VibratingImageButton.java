package com.berniac.vocalwarmup.ui.player;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

/**
 * Created by Mikhail Lipkovich on 8/14/2018.
 */
public class VibratingImageButton extends AppCompatImageButton {

    private static final int VIBRATE_LENGTH_MS = 20;
    private Vibrator vibe;

    public VibratingImageButton(Context context) {
        super(context);
        vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public VibratingImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public VibratingImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public boolean performClick() {
        vibe.vibrate(VIBRATE_LENGTH_MS);
        return super.performClick();
    }
}
