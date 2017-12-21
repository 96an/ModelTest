package com.example.eban.modeltest.Activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eban.modeltest.R;
import com.example.eban.modeltest.common.Common;
import com.squareup.picasso.Picasso;

public class Playing extends AppCompatActivity implements View.OnClickListener {

    final static long INTERVAL = 1000;
    final static long TIMEOUT = 7000;
    int progressValue = 0;

    CountDownTimer mCountDownTimer;

    int index = 0, score = 0, thisQuestion = 0, totalQuestion, correctQuestion;


    ProgressBar mProgressBar;
    ImageView question_image;
    TextView txtScore, txtQuestionNum, question_text;
    Button btnA, btnB, btnC, btnD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        txtScore = findViewById(R.id.txtScore);
        txtQuestionNum = findViewById(R.id.txtTotalQuestion);
        question_text = findViewById(R.id.questionText);
        question_image = findViewById(R.id.questionImage);

        mProgressBar = findViewById(R.id.progressBar);
        btnA = findViewById(R.id.btnAnswerA);
        btnB = findViewById(R.id.btnAnswerB);
        btnC = findViewById(R.id.btnAnswerC);
        btnD = findViewById(R.id.btnAnswerD);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        mCountDownTimer.cancel();

        if (index < totalQuestion) {
            Button clickedButton = (Button) view;
            if (clickedButton.getText().equals(Common.questionList.get(index).getCorrectAnswer())) {
                score += 10;
                correctQuestion++;
                showQuestion(++index);
            } else {

                Intent intent = new Intent(this, Done.class);
                Bundle dataSend = new Bundle();
                dataSend.putInt("SCORE", score);
                dataSend.putInt("TOTAL", totalQuestion);
                dataSend.putInt("CORRECT", correctQuestion);
                intent.putExtras(dataSend);
                startActivity(intent);
                finish();

            }

            txtScore.setText(String.format("%d", score));
        }
    }

    private void showQuestion(int index) {
        if (index < totalQuestion) {
            thisQuestion++;
            txtQuestionNum.setText(String.format("%d / %d", thisQuestion, totalQuestion));
            mProgressBar.setProgress(0);
            progressValue = (0);


            if (Common.questionList.get(index).getIsImageQuestion().equals("true")) {
                Picasso.with(getBaseContext())
                        .load(Common.questionList.get(index).getQuestion())
                        .into(question_image);

                question_image.setVisibility(View.VISIBLE);
                question_text.setVisibility(View.INVISIBLE);
            } else {
                question_text.setText(Common.questionList.get(index).getQuestion());
                question_image.setVisibility(View.INVISIBLE);
                question_text.setVisibility(View.VISIBLE);
            }

            btnA.setText(Common.questionList.get(index).getAnswerA());
            btnB.setText(Common.questionList.get(index).getAnswerB());
            btnC.setText(Common.questionList.get(index).getAnswerC());
            btnD.setText(Common.questionList.get(index).getAnswerD());

            mCountDownTimer.start();
        }
        else {
            Intent intent = new Intent(this, Done.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("SCORE", score);
            dataSend.putInt("TOTAL", totalQuestion);
            dataSend.putInt("CORRECT", correctQuestion);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        totalQuestion = Common.questionList.size();

        mCountDownTimer = new CountDownTimer(TIMEOUT,INTERVAL) {
            @Override
            public void onTick(long i) {
                mProgressBar.setProgress(progressValue);
                progressValue++;

            }
            @Override
            public void onFinish() {
                mCountDownTimer.cancel();
                showQuestion(++index);
            }
        };
        showQuestion(index);
    }
}
