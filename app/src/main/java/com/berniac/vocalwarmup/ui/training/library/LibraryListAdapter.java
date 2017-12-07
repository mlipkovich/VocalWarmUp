package com.berniac.vocalwarmup.ui.training.library;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.berniac.vocalwarmup.R;
import com.berniac.vocalwarmup.ui.ResourcesProvider;
import com.berniac.vocalwarmup.ui.training.ItemRowView;

/**
 * Created by Mikhail Lipkovich on 11/25/2017.
 */
public class LibraryListAdapter extends LibraryListView<LibraryListAdapter.ViewHolder> {

    private Context context;
    private LibraryListPresenter presenter;

    public class ViewHolder extends RecyclerView.ViewHolder implements ItemRowView {
        private TextView titleView;
        private ImageView imageView;

        public ViewHolder(View view, int viewType) {
            super(view);
            if (viewType == getCategoriesViewType()) {
                titleView = (TextView) view.findViewById(R.id.category_name);
                imageView = (ImageView) view.findViewById(R.id.category_image);
            } else {
                titleView = (TextView) view.findViewById(R.id.draw_name);
                imageView = (ImageView) view.findViewById(R.id.draw_image);
            }
        }

        @Override
        public void setTitle(String title) {
            titleView.setText(title);
        }

        @Override
        public void setImage(String imageName) {
            int imageId = ResourcesProvider.getDrawable(context, imageName);
            imageView.setImageResource(imageId);
        }
    }

    public LibraryListAdapter(LibraryListPresenter presenter, Context context) {
        this.presenter = presenter;
        this.presenter.onAttach(this);
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return presenter.getItemsType();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View itemView;

        if (viewType == getCategoriesViewType()) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_category, parent, false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecyclerView list = (RecyclerView) parent;
                    int clickedItemPosition = list.getChildLayoutPosition(v);
                    presenter.onCategoryClicked(clickedItemPosition);
                }
            });
        } else if (viewType == getDrawsViewType()) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_draw, parent, false);
        } else {
            throw new IllegalStateException("There is no view type for " + viewType);
        }

        return new ViewHolder(itemView, viewType);
    }

    @Override
    public void switchToActivityWithCategory(String clickedItemName) {
        Intent intent = new Intent(context, LibraryCategoryActivity.class);
        intent.putExtra(LibraryCategoryActivity.CATEGORY_NAME_PARAM, clickedItemName);
        context.startActivity(intent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        presenter.onBindCategoryAtPosition(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getItemsCount();
    }
}
