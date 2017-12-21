package com.example.eban.modeltest.BroadcastReceiver;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.eban.modeltest.common.Common;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by eban on 12/20/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        handleNotification(remoteMessage.getNotification().getBody());
    }

    private void handleNotification(String body) {

        Intent intent = new Intent(Common.STR_PUSH);
        intent.putExtra("message",body);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
