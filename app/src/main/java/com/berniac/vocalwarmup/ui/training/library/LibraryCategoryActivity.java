package com.berniac.vocalwarmup.ui.training.library;

import android.os.Bundle;

import com.berniac.vocalwarmup.R;
import com.berniac.vocalwarmup.ui.BottomNavigationActivity;

/**
 * Created by Mikhail Lipkovich on 12/5/2017.
 */
public class LibraryCategoryActivity extends BottomNavigationActivity  {

    public static final String CATEGORY_NAME_PARAM = "category_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LibraryFragment libraryFragment =
                (LibraryFragment) getSupportFragmentManager().findFragmentById(R.id.library_fragment);
        LibraryListPresenter libraryListPresenter = libraryFragment.getPresenter();
        libraryListPresenter.setCategories(SelectedCategories.INSTANCE.getCategoryIds());
    }

    @Override
    protected String getScreenTitle() {
        return getIntent().getExtras().getString(CATEGORY_NAME_PARAM);
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
        topBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        ((LibraryCategoryPresenter) presenter).onSupportNavigateUp();
        return true;
    }

    @Override
    public void onBackPressed() {
        ((LibraryCategoryPresenter) presenter).onBackPressed();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_library_category;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.menu_training;
    }

    @Override
    protected void createPresenter() {
        presenter = new LibraryCategoryPresenter();
    }
}
