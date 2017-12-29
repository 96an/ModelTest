package com.example.eban.modeltest.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eban.modeltest.Model.QuestionScore;
import com.example.eban.modeltest.R;
import com.example.eban.modeltest.common.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Done extends AppCompatActivity {

    Button tryAgain;
    TextView txtTotalScore,txtTotalQuestion;
    ProgressBar mProgressBar;

    FirebaseDatabase mDatabase;
    DatabaseReference question_score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        mDatabase = FirebaseDatabase.getInstance();
        question_score= mDatabase.getReference("Question_Score");

        txtTotalQuestion =findViewById(R.id.txtTotalQuestion);
        txtTotalScore = findViewById(R.id.txtTotalScore);
        tryAgain = findViewById(R.id.btnTryAgain);
        mProgressBar = findViewById(R.id.doneProgressBar);

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Done.this,Home.class);
                startActivity(intent);
                finish();
            }
        });

        Bundle extra = getIntent().getExtras();
        if (extra !=null){
            int score = extra.getInt("SCORE");
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECT");

            txtTotalScore.setText(String.format("Score : %d",score));
            txtTotalQuestion.setText(String.format("Passed : %d / %d",correctAnswer,totalQuestion));

            mProgressBar.setMax(totalQuestion);
            mProgressBar.setProgress(correctAnswer);

            question_score.child(String.format("%s_%s",
                    Common.currentUser.getUserName(),Common.categoryId))
                    .setValue(new QuestionScore(String.format("%s_s",
                            Common.currentUser.getUserName(),
                            Common.categoryId),
                            Common.currentUser.getUserName(),
                            String.valueOf(score),
                            Common.categoryId,
                            Common.categoryName));
        }
    }
}
