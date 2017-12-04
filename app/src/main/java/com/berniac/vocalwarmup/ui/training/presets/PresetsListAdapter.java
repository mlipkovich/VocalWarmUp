package com.berniac.vocalwarmup.ui.training.presets;

import android.content.Context;
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
public class PresetsListAdapter extends RecyclerView.Adapter<PresetsListAdapter.ViewHolder> {

    private PresetsListPresenter presenter;
    private Context context;

    // TODO: Reuse relevant parts from LibraryListAdapter?
    public class ViewHolder extends RecyclerView.ViewHolder implements ItemRowView {
        private TextView titleView;
        private ImageView imageView;

        public ViewHolder(View view){
            super(view);
            titleView = (TextView) view.findViewById(R.id.draw_name);
            imageView = (ImageView) view.findViewById(R.id.draw_image);
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

    public PresetsListAdapter(PresetsListPresenter presenter, Context context){
        this.presenter = presenter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_draw, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        presenter.onBindDrawAtPosition(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getDrawsCount();
    }
}
