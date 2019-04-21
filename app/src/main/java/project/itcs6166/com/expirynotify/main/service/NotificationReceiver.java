package project.itcs6166.com.expirynotify.main.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.Timestamp;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import project.itcs6166.com.expirynotify.R;
import project.itcs6166.com.expirynotify.main.common.ItemData;
import project.itcs6166.com.expirynotify.main.list.ShowListActivity;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "ExpNotify";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
            Intent myIntent = new Intent(context, ShowListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 101, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            List<Map<String, Object>> itemData = ItemData.getItemData();
            for(Map<String, Object> item : itemData){
                Timestamp timestamp = (Timestamp)item.get("exp_date");
                String curDate = DateFormat.getDateInstance().format(new Date());
                String exp_date = DateFormat.getDateInstance().format(timestamp.toDate());


                if(exp_date.equals(curDate)){
                    String label = (String) item.get("label");

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentTitle("SmartExpiry Notification")
                            .setContentText(label + " is expiring today!")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);

                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    // notificationId is a unique int for each notification that you must define


                    Log.d("Notify", "Alarm");

                    notificationManager.notify(new Random().nextInt(), builder.build());
                }
            }
    }
}
