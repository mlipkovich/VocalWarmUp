package com.berniac.vocalwarmup.ui.saved;


import com.berniac.vocalwarmup.R;
import com.berniac.vocalwarmup.ui.BottomNavigationActivity;

/**
 * Created by Mikhail Lipkovich on 11/21/2017.
 */
public class SavedActivity extends BottomNavigationActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_saved;
    }

    @Override
    public String getScreenTitle() {
        return "Сохраненные распевки";
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.menu_saved;
    }

    @Override
    protected void createPresenter() {
        presenter = new SavedPresenter();
    }
}
