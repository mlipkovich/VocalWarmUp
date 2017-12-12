package com.berniac.vocalwarmup.ui.training.presets;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.berniac.vocalwarmup.R;
import com.berniac.vocalwarmup.ui.PlayerActivity;
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
        this.presenter.onAttach(this);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_draw, parent, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView list = (RecyclerView) parent;
                int clickedItemPosition = list.getChildLayoutPosition(v);
                presenter.onItemClicked(clickedItemPosition);
            }
        });

        return new ViewHolder(itemView);
    }

    // TODO: Common interface for this and categories adapter with this method
    public void switchToPlayer() {
        Intent intent = new Intent(context, PlayerActivity.class);
        context.startActivity(intent);
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
