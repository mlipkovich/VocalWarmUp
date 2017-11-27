package com.berniac.vocalwarmup.ui.training;

import com.berniac.vocalwarmup.ui.data.IWarmUpRepository;
import com.berniac.vocalwarmup.ui.data.WarmUpRepository;
import com.berniac.vocalwarmup.ui.training.categories.CategoryListPresenter;

/**
 * Created by Mikhail Lipkovich on 11/28/2017.
 */
public class LibraryPresenter {

    private LibraryFragment view;
    private IWarmUpRepository warmUpRepository = new WarmUpRepository();

    public void onAttach(LibraryFragment view) {
        this.view = view;
    }

    public CategoryListPresenter getCategoryListPresenter() {
        return new CategoryListPresenter(warmUpRepository);
    }
}
