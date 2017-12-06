package com.berniac.vocalwarmup.ui;

/**
 * Created by Mikhail Lipkovich on 11/27/2017.
 */
public abstract class BottomNavigationPresenter {

    protected BottomNavigationActivity view;
    private static final int MENU_SWITCH_DELAY_MS = 200;

    public void onAttach(BottomNavigationActivity view) {
        this.view = view;
    }

    public void onViewCreated() {}

    public void onStart() {
        view.updateNavigationBarState();
    }

    public void onPause() {
        view.overridePendingTransition();
    }

    public void onTrainingMenuSelected() {
        view.switchToTraining(MENU_SWITCH_DELAY_MS);
    }

    public void onSavedMenuSelected() {
        view.switchToSaved(MENU_SWITCH_DELAY_MS);
    }

    public void onSettingsMenuSelected() {
        view.switchToSettings(MENU_SWITCH_DELAY_MS);
    }

    public void onCurrentMenuSelected() {
        // do nothing
    }
}
