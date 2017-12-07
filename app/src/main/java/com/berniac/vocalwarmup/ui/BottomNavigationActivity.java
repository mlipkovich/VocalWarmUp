package com.berniac.vocalwarmup.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.berniac.vocalwarmup.R;
import com.berniac.vocalwarmup.ui.saved.SavedActivity;
import com.berniac.vocalwarmup.ui.settings.SettingsActivity;
import com.berniac.vocalwarmup.ui.training.TrainingActivity;

/**
 * Created by Mikhail Lipkovich on 11/21/2017.
 */
public abstract class BottomNavigationActivity extends AppCompatActivity {

    protected BottomNavigationView bottomMenu;
    protected ActionBar topBar;
    protected BottomNavigationPresenter presenter;

    protected abstract int getContentViewId();

    protected abstract int getNavigationMenuItemId();

    protected abstract void createPresenter();

    protected abstract String getScreenTitle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        attachPresenter();

        bottomMenu = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomMenu.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == getNavigationMenuItemId()) {
                    presenter.onCurrentMenuSelected();
                } else if (itemId == R.id.menu_training) {
                    presenter.onTrainingMenuSelected();
                } else if (itemId == R.id.menu_saved) {
                    presenter.onSavedMenuSelected();
                } else if (itemId == R.id.menu_settings) {
                    presenter.onSettingsMenuSelected();
                }
                return true;
            }
        });

        initActionBar();
        presenter.onViewCreated();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    protected void initActionBar() {
        topBar = getSupportActionBar();
        topBar.setTitle(getScreenTitle());
    }

    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    public void overridePendingTransition() {
        overridePendingTransition(0, 0);
    }

    public void switchToTraining(int postDelay) {
        startActivityWithPostDelay(TrainingActivity.class, postDelay);
    }

    public void switchToSettings(int postDelay) {
        startActivityWithPostDelay(SettingsActivity.class, postDelay);
    }

    public void switchToSaved(int postDelay) {
        startActivityWithPostDelay(SavedActivity.class, postDelay);
    }

    public void updateNavigationBarState(){
        int itemId = getNavigationMenuItemId();
        Menu menu = bottomMenu.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            boolean shouldBeChecked = item.getItemId() == itemId;
            if (shouldBeChecked) {
                item.setChecked(true);
                return;
            }
        }
    }

    private void startActivityWithPostDelay(final Class activity, int postDelay) {
        bottomMenu.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), activity));
                finish();
            }
        }, postDelay);
    }

    private void attachPresenter() {
        createPresenter();
        presenter.onAttach(this);
    };
}