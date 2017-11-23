package com.berniac.vocalwarmup;

/**
 * Created by Mikhail Lipkovich on 11/21/2017.
 */
public class SettingsActivity extends BottomNavigationActivity {
    @Override
    int getContentViewId() {
        return R.layout.activity_settings;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.menu_settings;
    }

    @Override
    String getScreenTitle() {
        return "Настройки";
    }
}
