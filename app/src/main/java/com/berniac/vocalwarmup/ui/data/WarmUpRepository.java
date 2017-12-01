package com.berniac.vocalwarmup.ui.data;

import com.berniac.vocalwarmup.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikhail Lipkovich on 11/27/2017.
 */
public class WarmUpRepository implements IWarmUpRepository {

    // TODO: Move all below urodstvo to appropriate model, do not depend on R
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

        // TODO: For what you need 'playButtonId'? They all have the same icon, no?
        // Now the icon doesn't show and I don't understand why.
        WARM_UP_PATTERNS.add(new WarmUpPattern("Сигарета после проливного дождя во Франкфурте", R.drawable.pattern_visualisation, R.drawable.listen_pattern_button));
        WARM_UP_PATTERNS.add(new WarmUpPattern("Круассан с кофе у океана", R.drawable.pattern_visualisation, R.drawable.listen_pattern_button));
        WARM_UP_PATTERNS.add(new WarmUpPattern("В стиле музыки французских улиц", R.drawable.pattern_visualisation, R.drawable.listen_pattern_button));
    }

    public int getCategoriesCount() {
        return WARM_UP_CATEGORIES.size();
    }

    public int getPatternsCount() {
        return WARM_UP_PATTERNS.size();
    }

    public List<WarmUpCategory> getCategories() {
        return WARM_UP_CATEGORIES;
    }

    public List<WarmUpPattern> getPatterns() {
        return WARM_UP_PATTERNS;
    }
}
