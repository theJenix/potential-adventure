package com.punventure.punadventure.notify;

import com.punventure.punadventure.NoteListActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationClient {

    private Context context;
    private static final int NEW_NOTES_ID = 100;

    public NotificationClient(Context context) {
        this.context = context;
    }
    public void notifyNewNotes() {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this.context, NoteListActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this.context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(android.R.drawable.btn_star)
               .setContentTitle("You've found a new note!")
               .setContentText("Click to view your new note!")
               .setContentIntent(contentIntent)
               .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        //Pass the Notification to the NotificationManager:

        Notification n = builder.build();
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(NEW_NOTES_ID, n); 
    }
}
