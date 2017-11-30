package com.berniac.vocalwarmup.ui.data;

import java.util.List;

/**
 * Created by Mikhail Lipkovich on 11/28/2017.
 */
public interface IWarmUpRepository {

    List<WarmUpCategory> getCategories();

    List<WarmUpPattern> getPatterns();

    int getCategoriesCount();

    int getPatternsCount();
}
