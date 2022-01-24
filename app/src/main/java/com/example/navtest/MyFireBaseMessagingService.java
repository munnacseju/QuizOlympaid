package com.example.navtest;


import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getNotification()!=null){
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            NotificationHandle.displayNotification(getApplicationContext(), title, body);

            Toast.makeText(getApplicationContext(), "Notification is comming", Toast.LENGTH_SHORT).show();
        }

    }
}
