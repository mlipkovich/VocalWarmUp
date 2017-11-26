package com.berniac.vocalwarmup;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikhail Lipkovich on 11/21/2017.
 */
public class TrainingActivity extends BottomNavigationActivity {

    // TODO: Move all below urodstvo to Model. Pasha, please don't judge me
    private static final List<WarmUpCategory> WARM_UP_CATEGORIES = new ArrayList<>();
    private static final List<WarmUpPattern> WARM_UP_PATTERNS = new ArrayList<>();

    static {
        WARM_UP_CATEGORIES.add(new WarmUpCategory("Для новичков_1", R.drawable.ic_category));
        WARM_UP_CATEGORIES.add(new WarmUpCategory("Для новичков_2", R.drawable.ic_category));
        WARM_UP_CATEGORIES.add(new WarmUpCategory("Для новичков_3", R.drawable.ic_category));
        WARM_UP_CATEGORIES.add(new WarmUpCategory("Для продолжающих_1", R.drawable.ic_category));
        WARM_UP_CATEGORIES.add(new WarmUpCategory("Для продолжающих_2", R.drawable.ic_category));
        WARM_UP_CATEGORIES.add(new WarmUpCategory("Для продолжающих_3", R.drawable.ic_category));
        WARM_UP_CATEGORIES.add(new WarmUpCategory("Для профессионалов_1", R.drawable.ic_category));
        WARM_UP_CATEGORIES.add(new WarmUpCategory("Для профессионалов_2", R.drawable.ic_category));
        WARM_UP_CATEGORIES.add(new WarmUpCategory("Для профессионалов_3", R.drawable.ic_category));
        WARM_UP_CATEGORIES.add(new WarmUpCategory("Для Ильи", R.drawable.ic_category));

        WARM_UP_PATTERNS.add(new WarmUpPattern("Сигарета после проливного дождя во Франкфурте", R.drawable.pattern_visualisation, 0));
        WARM_UP_PATTERNS.add(new WarmUpPattern("Круассан с кофе у океана", R.drawable.pattern_visualisation, 0));
        WARM_UP_PATTERNS.add(new WarmUpPattern("В стиле музыки французских улиц", R.drawable.pattern_visualisation, 0));
    }


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
            RecyclerView warmUpList = (RecyclerView) view.findViewById(R.id.listPresets);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            warmUpList.setLayoutManager(layoutManager);
            warmUpList.setItemAnimator(new DefaultItemAnimator());
            warmUpList.setHasFixedSize(true);

            WarmUpPatternAdapter patternAdapter = new WarmUpPatternAdapter(WARM_UP_PATTERNS);
            warmUpList.setAdapter(patternAdapter);
            return view;
        }
    }

    public static class LibraryFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_library, container, false);
            RecyclerView categoryList = (RecyclerView) view.findViewById(R.id.list_categories);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            categoryList.setLayoutManager(layoutManager);
            categoryList.setItemAnimator(new DefaultItemAnimator());
            categoryList.setHasFixedSize(true);
            WarmUpCategoryAdapter categoryAdapter = new WarmUpCategoryAdapter(WARM_UP_CATEGORIES);
            categoryList.setAdapter(categoryAdapter);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(categoryList.getContext(), layoutManager.getOrientation());
            categoryList.addItemDecoration(dividerItemDecoration);

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
