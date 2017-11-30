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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikhail Lipkovich on 11/21/2017.
 */
public class TrainingActivity extends BottomNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager_training);
        viewPager.setAdapter(new TrainingPageAdapter(getSupportFragmentManager(), this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_training);
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

    private class TrainingPageAdapter extends FragmentStatePagerAdapter {

        private String tabTitles[] = new String[] {"БЫСТРЫЙ СТАРТ", "БИБЛИОТЕКА"};
        private List<Fragment> fragments;

        // TODO: Ugly. Think about better architecture
        TrainingPageAdapter(FragmentManager fm, Context context) {
            super(fm);
            QuickStartFragment quickStartFragment =
                    (QuickStartFragment) Fragment.instantiate(context, QuickStartFragment.class.getName());
            quickStartFragment.setPresenter(new QuickStartPresenter());
            LibraryFragment libraryFragment =
                    (LibraryFragment) Fragment.instantiate(context, LibraryFragment.class.getName());
            libraryFragment.setPresenter(new LibraryPresenter());

            fragments = new ArrayList<>();
            fragments.add(quickStartFragment);
            fragments.add(libraryFragment);
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
