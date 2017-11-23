package com.berniac.vocalwarmup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Mikhail Lipkovich on 11/21/2017.
 */
public abstract class BottomNavigationActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    protected BottomNavigationView bottomMenu;
    protected ActionBar topBar;
    private static final int MENU_SWITCH_DELAY_MS = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        bottomMenu = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomMenu.setOnNavigationItemSelectedListener(this);

        topBar = getSupportActionBar();
        topBar.setTitle(getScreenTitle());
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }

    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final int itemId = item.getItemId();
        if (itemId == getNavigationMenuItemId()) {
            return true;
        }
        bottomMenu.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (itemId == R.id.menu_training) {
                    startActivity(new Intent(getApplicationContext(), TrainingActivity.class));
                } else if (itemId == R.id.menu_saved) {
                    startActivity(new Intent(getApplicationContext(), SavedActivity.class));
                } else if (itemId == R.id.menu_settings) {
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                }
                finish();
            }
        }, MENU_SWITCH_DELAY_MS);
        return true;
    }

    private void updateNavigationBarState(){
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        Menu menu = bottomMenu.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            boolean shouldBeChecked = item.getItemId() == itemId;
            if (shouldBeChecked) {
                item.setChecked(true);
                break;
            }
        }
    }

    abstract int getContentViewId();

    abstract int getNavigationMenuItemId();

    abstract String getScreenTitle();
}