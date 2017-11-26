package com.berniac.vocalwarmup;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mikhail Lipkovich on 11/25/2017.
 */
public class WarmUpPatternAdapter extends RecyclerView.Adapter<WarmUpPatternAdapter.ViewHolder> {

    private List<WarmUpPattern> patternList;

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private ImageView image;
        private ImageButton button;

        public ViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.pattern_name);
            image = (ImageView) view.findViewById(R.id.pattern_image);
            button = (ImageButton) view.findViewById(R.id.pattern_play_btn);
        }
    }

    public WarmUpPatternAdapter(List<WarmUpPattern> patternList){
        this.patternList = patternList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_warm_up_pattern, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WarmUpPattern item = patternList.get(position);
        holder.name.setText(item.getName());
        holder.image.setImageResource(item.getImageId());
//        holder.button.;
    }

    @Override
    public int getItemCount() {
        return patternList.size();
    }
}
