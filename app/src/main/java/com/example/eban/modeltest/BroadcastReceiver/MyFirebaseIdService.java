package com.example.eban.modeltest.BroadcastReceiver;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by eban on 12/20/17.
 */

public class MyFirebaseIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedId = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(refreshedId);
    }

    private void sendRegistrationToServer(String refreshedId) {
    }
}
