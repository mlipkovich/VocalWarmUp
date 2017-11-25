package com.berniac.vocalwarmup;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mikhail Lipkovich on 11/25/2017.
 */
public class WarmUpCategoryAdapter extends RecyclerView.Adapter<WarmUpCategoryAdapter.ViewHolder> {

    private List<WarmUpCategory> categoryList;

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private ImageView image;

        public ViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.category_name);
            image = (ImageView) view.findViewById(R.id.category_image);
        }
    }

    public WarmUpCategoryAdapter(List<WarmUpCategory> categoryList){
        this.categoryList = categoryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_warm_up_category, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WarmUpCategory item = categoryList.get(position);
        holder.name.setText(item.getName());
        holder.image.setImageResource(item.getImageId());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
