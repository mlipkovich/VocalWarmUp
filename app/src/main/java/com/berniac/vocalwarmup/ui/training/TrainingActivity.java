package com.berniac.vocalwarmup.ui.training;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.berniac.vocalwarmup.R;
import com.berniac.vocalwarmup.ui.BottomNavigationActivity;
import com.berniac.vocalwarmup.ui.training.library.LibraryFragment;
import com.berniac.vocalwarmup.ui.training.presets.PresetsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikhail Lipkovich on 11/21/2017.
 */
public class TrainingActivity extends BottomNavigationActivity {

    private TrainingPageAdapter pageAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager_training);
        pageAdapter = new TrainingPageAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pageAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs_training);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        topBar = getSupportActionBar();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_training;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.menu_training;
    }

    @Override
    protected void createPresenter() {
        presenter = new TrainingPresenter();
    }

    @Override
    public void onBackPressed() {
        // TODO: Probably this logic should be somehow moved to presenter
        TrainingFragment libraryFragment =
                pageAdapter.fragments.get(TrainingPageAdapter.LIBRARY_INDEX);
        if (tabLayout.getSelectedTabPosition() == TrainingPageAdapter.LIBRARY_INDEX) {
            libraryFragment.onBackButtonClicked();
        } else {
            super.onBackPressed();
        }
    }

    private class TrainingPageAdapter extends FragmentStatePagerAdapter {

        private static final int PRESETS_INDEX = 0;
        private static final int LIBRARY_INDEX = 1;
        private String tabTitles[] = new String[] {"БЫСТРЫЙ СТАРТ", "БИБЛИОТЕКА"};
        private List<TrainingFragment> fragments;

        TrainingPageAdapter(FragmentManager fm, Context context) {
            super(fm);
            PresetsFragment presetsFragment =
                    (PresetsFragment) Fragment.instantiate(context, PresetsFragment.class.getName());
            LibraryFragment libraryFragment =
                    (LibraryFragment) Fragment.instantiate(context, LibraryFragment.class.getName());

            fragments = new ArrayList<>();
            fragments.add(PRESETS_INDEX, presetsFragment);
            fragments.add(LIBRARY_INDEX, libraryFragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}
