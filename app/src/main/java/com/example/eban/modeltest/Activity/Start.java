package com.example.eban.modeltest.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.eban.modeltest.Model.Question;
import com.example.eban.modeltest.R;
import com.example.eban.modeltest.common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class Start extends AppCompatActivity {

    Button btnPlay;

    FirebaseDatabase mDatabase;
    DatabaseReference questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mDatabase = FirebaseDatabase.getInstance();
        questions = mDatabase.getReference("Questions");

        btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Start.this,Playing.class);
                startActivity(intent);
            }
        });

        loadQuestions(Common.categoryId);
    }

    private void loadQuestions(String categoryId) {

        if (Common.questionList.size() > 0) {

            Common.questionList.clear();

        }
        questions.orderByChild("CategoryId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    Question question = postSnapShot.getValue(Question.class);
                    Common.questionList.add(question);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Collections.shuffle(Common.questionList);
    }
}
