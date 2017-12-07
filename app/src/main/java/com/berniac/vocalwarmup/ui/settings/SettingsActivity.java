package com.berniac.vocalwarmup.ui.settings;


import com.berniac.vocalwarmup.R;
import com.berniac.vocalwarmup.ui.BottomNavigationActivity;

/**
 * Created by Mikhail Lipkovich on 11/21/2017.
 */
public class SettingsActivity extends BottomNavigationActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_settings;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.menu_settings;
    }

    @Override
    public String getScreenTitle() {
        return "Настройки";
    }

    @Override
    protected void createPresenter() {
        presenter = new SettingsPresenter();
    }
}
