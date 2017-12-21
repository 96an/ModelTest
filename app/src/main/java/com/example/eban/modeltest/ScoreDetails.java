package com.example.eban.modeltest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.eban.modeltest.Model.QuestionScore;
import com.example.eban.modeltest.ViewHolder.ScoreDetailViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ScoreDetails extends AppCompatActivity {

    FirebaseDatabase mDatabase;
    DatabaseReference questionScore;

    String viewUser = "";

    RecyclerView scoreList;
    RecyclerView.LayoutManager mLayoutManager;
    FirebaseRecyclerAdapter<QuestionScore, ScoreDetailViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_details);

        mDatabase = FirebaseDatabase.getInstance();
        questionScore = mDatabase.getReference("Question_Score");

        //view
        scoreList = findViewById(R.id.scoreList);
        scoreList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        scoreList.setLayoutManager(mLayoutManager);

        if (getIntent() != null) {
            viewUser = getIntent().getStringExtra("viewUser");
        }
        if (!viewUser.isEmpty()) {
            loadScoreDetails(viewUser);
        }
    }

    private void loadScoreDetails(String viewUser) {

        mAdapter = new FirebaseRecyclerAdapter<QuestionScore, ScoreDetailViewHolder>(
                QuestionScore.class,
                R.layout.score_detail_layout,
                ScoreDetailViewHolder.class,
                questionScore.orderByChild("user").equalTo(viewUser)

        ) {
            @Override
            protected void populateViewHolder(ScoreDetailViewHolder viewHolder, QuestionScore model, int position) {
                viewHolder.txtCategory.setText(model.getCategoryName());
                viewHolder.txtScore.setText(model.getScore());
            }
        };
        mAdapter.notifyDataSetChanged();
        scoreList.setAdapter(mAdapter);
    }
}
