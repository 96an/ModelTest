package com.example.eban.modeltest.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eban.modeltest.Interface.ItemClickListener;
import com.example.eban.modeltest.Interface.RankingCallBack;
import com.example.eban.modeltest.Model.Question;
import com.example.eban.modeltest.Model.QuestionScore;
import com.example.eban.modeltest.Model.Ranking;
import com.example.eban.modeltest.R;
import com.example.eban.modeltest.ScoreDetails;
import com.example.eban.modeltest.ViewHolder.RankingViewHolder;
import com.example.eban.modeltest.common.Common;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RankingFragment extends Fragment {

    View myFragment;

    FirebaseDatabase mDatabase;
    DatabaseReference questionScore;
    DatabaseReference rankingTbl;

    RecyclerView rankingList;
    LinearLayoutManager mLayoutManager;
    FirebaseRecyclerAdapter<Ranking, RankingViewHolder> mAdapter;

    int sum = 0;

    public static RankingFragment newInstance() {
        RankingFragment rankingFragment = new RankingFragment();
        return rankingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance();
        questionScore = mDatabase.getReference("Question_Score");
        rankingTbl = mDatabase.getReference("Ranking");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_ranking, container, false);

        rankingList = myFragment.findViewById(R.id.rankingList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rankingList.setHasFixedSize(true);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(mLayoutManager);

        updateScore(Common.currentUser.getUserName(), new RankingCallBack<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
                rankingTbl.child(ranking.getUserName())
                        .setValue(ranking);
            }
        });

        mAdapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(
                Ranking.class,
                R.layout.ranking_layout,
                RankingViewHolder.class,
                rankingTbl.orderByChild("score")

        ) {
            @Override
            protected void populateViewHolder(RankingViewHolder viewHolder, final Ranking model, int position) {
                viewHolder.txtName.setText(model.getUserName());
                viewHolder.txtScore.setText(String.valueOf(model.getScore()));

                viewHolder.setClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent scoreDetail = new Intent(getActivity(), ScoreDetails.class);
                        scoreDetail.putExtra("viewUser",model.getUserName());
                        startActivity(scoreDetail);
                    }
                });
            }
        };

        mAdapter.notifyDataSetChanged();
        rankingList.setAdapter(mAdapter);
        return myFragment;
    }


    private void updateScore(final String userName, final RankingCallBack<Ranking> callback) {

        questionScore.orderByChild("user").equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    QuestionScore ques = data.getValue(QuestionScore.class);
                    sum += Integer.parseInt(ques.getScore());

                    Ranking ranking = new Ranking(userName,sum);
                    callback.callBack(ranking);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
