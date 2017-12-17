package com.example.eban.modeltest;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.eban.modeltest.Fragment.CategoryFragment;
import com.example.eban.modeltest.Fragment.RankingFragment;

public class Home extends AppCompatActivity {

    BottomNavigationView mNavigationItemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNavigationItemView = findViewById(R.id.navigation_view);
        mNavigationItemView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectFragment = null;
                switch (item.getItemId()) {
                    case R.id.action_category:
                        selectFragment = CategoryFragment.newInstance();
                        break;
                    case R.id.action_ranking:
                        selectFragment = RankingFragment.newInstance();
                        break;
                }

                FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
                mTransaction.replace(R.id.frame_layout, selectFragment);
                mTransaction.commit();

                return true;
            }


        });

        setDefaultFragment();
    }

    private void setDefaultFragment() {
        FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.replace(R.id.frame_layout, CategoryFragment.newInstance());
        mTransaction.commit();
    }
}
