package com.example.eban.modeltest.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eban.modeltest.Interface.ItemClickListener;
import com.example.eban.modeltest.R;

/**
 * Created by Eban on 12/15/2017.
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView category_name;
    public ImageView category_image;
    ItemClickListener mItemClickListener;

    public CategoryViewHolder(View itemView) {
        super(itemView);

        category_name = itemView.findViewById(R.id.category_name);
        category_image = itemView.findViewById(R.id.category_image);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        mItemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
