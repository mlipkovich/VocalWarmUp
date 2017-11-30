package com.berniac.vocalwarmup.ui.training.categories;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.berniac.vocalwarmup.R;

/**
 * Created by Mikhail Lipkovich on 11/25/2017.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    // TODO: common interfaces for pattern/constructor adapters? Common interfaces for their presenters?
    private CategoryListPresenter presenter;

    public class ViewHolder extends RecyclerView.ViewHolder implements CategoryRowView {
        private TextView titleView;
        private ImageView imageView;

        public ViewHolder(View view){
            super(view);
            titleView = (TextView) view.findViewById(R.id.category_name);
            imageView = (ImageView) view.findViewById(R.id.category_image);
        }

        @Override
        public void setTitle(String title) {
            titleView.setText(title);
        }

        @Override
        public void setImage(int imageId) {
            imageView.setImageResource(imageId);
        }
    }

    public CategoryAdapter(CategoryListPresenter presenter){
        this.presenter = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_warm_up_category, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        presenter.onBindCategoryAtPosition(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getCategoriesCount();
    }
}
