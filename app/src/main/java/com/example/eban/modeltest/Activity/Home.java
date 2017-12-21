package com.example.eban.modeltest.Activity;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.eban.modeltest.Fragment.CategoryFragment;
import com.example.eban.modeltest.Fragment.RankingFragment;
import com.example.eban.modeltest.R;
import com.example.eban.modeltest.common.Common;

import java.util.Random;

public class Home extends AppCompatActivity {

    BottomNavigationView mNavigationItemView;

    BroadcastReceiver mBroadcastReceiver;


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,new IntentFilter("registrationComplete"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,new IntentFilter(Common.STR_PUSH));


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        registrationNotification();

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


    private void registrationNotification() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Common.STR_PUSH))
                {
                    String message = intent.getStringExtra("message");
                    showNotification("Eban",message);
                }
            }
        };
    }

    private void showNotification(String title, String message) {

        Intent intent= new Intent(getApplicationContext(),MainActivity.class);
        PendingIntent contentInten = PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(contentInten);

        NotificationManager notificationManager= (NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(),builder.build());

    }

    private void setDefaultFragment() {
        FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.replace(R.id.frame_layout, CategoryFragment.newInstance());
        mTransaction.commit();
    }
}
