package com.berniac.vocalwarmup;

import android.os.Bundle;

/**
 * Created by Mikhail Lipkovich on 11/21/2017.
 */
public class SavedActivity extends BottomNavigationActivity {

    @Override
    int getContentViewId() {
        return R.layout.activity_saved;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.menu_saved;
    }

    @Override
    String getScreenTitle() {
        return "Сохраненные распевки";
    }
}
