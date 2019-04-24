package project.itcs6166.com.expirynotify.main.service;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import project.itcs6166.com.expirynotify.R;
import project.itcs6166.com.expirynotify.main.common.ItemData;
import project.itcs6166.com.expirynotify.main.list.ShowListActivity;

import static android.content.Context.MODE_PRIVATE;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "ExpNotify";
    private List<Map<String, Object>>items;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
            Intent myIntent = new Intent(context, ShowListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 101, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            SharedPreferences sf = context.getSharedPreferences("sharedList", MODE_PRIVATE);
            Gson gson = new Gson();
            String itemList = sf.getString("savedItems", null);
            Type type = new TypeToken<ArrayList<Map<String, Object>>>(){}.getType();

            items = gson.fromJson(itemList, type);
            if(items == null){
                items = new ArrayList<>();
            }


            //List<Map<String, Object>> itemData = ItemData.getItemData();
            // compare current date with the expiry date of every item
            for(Map<String, Object> item : items){
                Map<String, Double> timesMap = (LinkedTreeMap<String, Double>)item.get("exp_date");
                double seconds = timesMap.get("seconds");
                double nanoseconds = timesMap.get("nanoseconds");
                Timestamp timestamp = new Timestamp((long)seconds, (int) nanoseconds);
                String curDate = DateFormat.getDateInstance().format(new Date());
                String exp_date = DateFormat.getDateInstance().format(timestamp.toDate());

                Date curr = new Date();
                long diff = timestamp.toDate().getTime() - curr.getTime();
                long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                if(days >=0 && days <= 2){
                    String label = (String) item.get("label");

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentTitle("SmartExpiry Notification")
                            .setContentText(label + " is expiring")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);

                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                    Log.d("Notify", "Alarm");
                    // notificationId is a unique int for each notification
                    notificationManager.notify(new Random().nextInt(), builder.build());
                }
            }
    }


}
