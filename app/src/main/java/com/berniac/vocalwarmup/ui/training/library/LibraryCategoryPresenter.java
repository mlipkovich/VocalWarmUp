package com.berniac.vocalwarmup.ui.training.library;

import com.berniac.vocalwarmup.ui.BottomNavigationPresenter;

/**
 * Created by Mikhail Lipkovich on 12/6/2017.
 */
public class LibraryCategoryPresenter extends BottomNavigationPresenter {

    public void onSupportNavigateUp() {
        if (!SelectedCategories.INSTANCE.isTopLevel()) {
            SelectedCategories.INSTANCE.goUpByHierarchy();
        }
        view.finish();
    }

    public void onBackPressed() {
        onSupportNavigateUp();
    }
}
