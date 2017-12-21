package com.example.eban.modeltest.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.eban.modeltest.R;

/**
 * Created by Eban on 12/19/2017.
 */

public class ScoreDetailViewHolder extends RecyclerView.ViewHolder {

    public TextView txtCategory,txtScore;

    public ScoreDetailViewHolder(View itemView) {
        super(itemView);

        txtCategory=itemView.findViewById(R.id.txtName);
        txtScore=itemView.findViewById(R.id.txtScore);
    }
}
