package com.berniac.vocalwarmup.ui.training.patterns;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.berniac.vocalwarmup.R;

/**
 * Created by Mikhail Lipkovich on 11/25/2017.
 */
public class PatternAdapter extends RecyclerView.Adapter<PatternAdapter.ViewHolder> {

    // TODO: common interfaces for pattern/constructor adapters? Common interfaces for their presenters?
    private PatternListPresenter presenter;

    public class ViewHolder extends RecyclerView.ViewHolder implements PatternRowView{
        private TextView titleView;
        private ImageView imageView;
        private ImageButton buttonView;

        public ViewHolder(View view){
            super(view);
            titleView = (TextView) view.findViewById(R.id.pattern_name);
            imageView = (ImageView) view.findViewById(R.id.pattern_image);
            buttonView = (ImageButton) view.findViewById(R.id.pattern_play_btn);
        }

        @Override
        public void setTitle(String title) {
            titleView.setText(title);
        }

        @Override
        public void setImage(int imageId) {
            imageView.setImageResource(imageId);
        }

        @Override
        public void setButton(int buttonId) {
            buttonView.setImageResource(buttonId);
        }
    }

    public PatternAdapter(PatternListPresenter presenter){
        this.presenter = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_warm_up_pattern, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        presenter.onBindPatternAtPosition(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getPatternsCount();
    }
}
