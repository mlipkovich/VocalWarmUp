package com.berniac.vocalwarmup.ui.training.library;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Mikhail Lipkovich on 12/4/2017.
 */
public abstract class LibraryListView<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    private static final int CATEGORIES_VIEW_TYPE = 1;
    private static final int DRAWS_VIEW_TYPE = 2;

    public abstract void onBackButtonClicked();

    public void reloadListItems() {
        notifyDataSetChanged();
    }

    public int getCategoriesViewType() {
        return CATEGORIES_VIEW_TYPE;
    }

    public int getDrawsViewType() {
        return DRAWS_VIEW_TYPE;
    }
}
