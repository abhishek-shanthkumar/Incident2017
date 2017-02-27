package in.co.inci17.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;

import in.co.inci17.R;
import in.co.inci17.auxiliary.Constants;
import in.co.inci17.auxiliary.Event;

public class EventReminder extends BroadcastReceiver {
    public EventReminder() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String eventString = intent.getStringExtra(Constants.EVENT_STRING);
        Gson gson = new Gson();
        Event event = gson.fromJson(eventString, Event.class);
        String startString = "Starting at "+event.getVenue() + " in " + Constants.BEFORE_MINUTES + " minutes";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(event.getTitle());
        builder.setContentText(startString);
        builder.setSmallIcon(R.drawable.ic_notification);

        Notification notification = new NotificationCompat.BigTextStyle(builder).bigText(startString+"\n"+event.getDescription()).build();
        notificationManager.notify(Integer.parseInt(event.getId()), notification);
    }
}
