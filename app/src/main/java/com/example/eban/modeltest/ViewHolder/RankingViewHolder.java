package com.example.eban.modeltest.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.eban.modeltest.Interface.ItemClickListener;
import com.example.eban.modeltest.R;

/**
 * Created by Eban on 12/18/2017.
 */

public class RankingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtName,txtScore;
    private ItemClickListener mClickListener;

    public void setTxtName(TextView txtName) {
        this.txtName = txtName;
    }

    public void setClickListener(ItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    public RankingViewHolder(View itemView) {
        super(itemView);

        txtName = itemView.findViewById(R.id.txt_name);
        txtScore = itemView.findViewById(R.id.txt_score);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mClickListener.onClick(view,getAdapterPosition(),false);
    }
}
