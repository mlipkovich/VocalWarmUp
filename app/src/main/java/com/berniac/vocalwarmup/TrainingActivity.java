package com.berniac.vocalwarmup;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikhail Lipkovich on 11/21/2017.
 */
public class TrainingActivity extends BottomNavigationActivity {

    // TODO: Do something with these hardcoded categories
    private static final String[] LIBRARY_CATEGORIES = new String[]{
            "Для новичков_1",
            "Для новичков_2",
            "Для новичков_3",
            "Для продолжающих_1",
            "Для продолжающих_2",
            "Для продолжающих_3",
            "Для любителей_1",
            "Для любителей_2",
            "Для любителей_3",
            "Для профессионалов_1",
            "Для профессионалов_2",
            "Для профессионалов_3",
            "Для Ильи"};

    // TODO: Do something with these hardcoded  presets
    private static final String[] QUICK_START_PRESETS = new String[]{
            "Сигарета после проливного дождя во Франкфурте",
            "Круассан с кофе у океана",
            "В стиле музыки французских улиц"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager_training);
        viewPager.setAdapter(new TrainingPageAdapter(getSupportFragmentManager(), this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_training);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_training;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.menu_training;
    }

    @Override
    String getScreenTitle() {
        return "Начало тренировки";
    }


    public static class QuickStartFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_quick_start, container, false);
            ListView warmUpList = (ListView) view.findViewById(R.id.listPresets);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1, QUICK_START_PRESETS);
            warmUpList.setAdapter(adapter);
            return view;
        }
    }

    public static class LibraryFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_library, container, false);
            ListView warmUpList = (ListView) view.findViewById(R.id.listCategories);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1, LIBRARY_CATEGORIES);
            warmUpList.setAdapter(adapter);
            return view;
        }
    }

    private class TrainingPageAdapter extends FragmentStatePagerAdapter {

        private String tabTitles[] = new String[] {"БЫСТРЫЙ СТАРТ", "БИБЛИОТЕКА"};
        private List<Fragment> fragments;
        private Context context;

        TrainingPageAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
            fragments = new ArrayList<>();
            fragments.add(Fragment.instantiate(context, QuickStartFragment.class.getName()));
            fragments.add(Fragment.instantiate(context, LibraryFragment.class.getName()));
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
