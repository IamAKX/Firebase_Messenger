package com.akash.applications.firebasemessenger;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.akash.applications.firebasemessenger.MainActivity;
import com.akash.applications.firebasemessenger.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import Adapters.ChatAdapter;
import HelperPackage.LocalPreferences;
import HelperPackage.TimeManager;
import LocalDatabase.MessageTracker;
import Models.AllUserListModel;

/**
 * Created by Akash on 01-12-2016.
 */

public class FcmMessagingService extends FirebaseMessagingService {
    MessageTracker messageTracker = new MessageTracker(this);
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        String name_of_user,id_of_user = null;
        name_of_user = title.substring(1+title.lastIndexOf(' '));

        Log.i("Checking","Before loop "+name_of_user);
        for(AllUserListModel obj : LocalPreferences.allUserListModelArrayList)
        {
            if(obj.getUserName().equals(title.substring(1+title.lastIndexOf(' '))))
            {
                id_of_user = obj.getUid();
                Log.i("Checking","Inside loop "+id_of_user);
            }

        }

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Log.i("Checking","Before saving");
        saveToLocalDB(title,body);
        Log.i("Checking","After saving");
        Intent intent = new Intent(this, BroadcastMessage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("friend_name",name_of_user);
        intent.putExtra("friend_id",id_of_user);
        intent.putExtra("message",body);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setSmallIcon(R.drawable.firebasemessage);
        builder.setAutoCancel(true);
        builder.setSound(soundUri);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());


    }

    private void saveToLocalDB(String title, String body) {
        String head="",id="";
        if(title.substring(0,5).equalsIgnoreCase("new b"))
            head = "Broadcast : "+title.substring(1+title.lastIndexOf(' '));
        else
            head = title.substring(1+title.lastIndexOf(' '));
        for(AllUserListModel obj : LocalPreferences.allUserListModelArrayList)
        {
            if(obj.getUserName().equals(title.substring(1+title.lastIndexOf(' '))))
                id = obj.getUid();
        }
        messageTracker.insertMessage(id,head,body, TimeManager.timeNow());
    }
}
