package com.example.bakalaurinis_v2.Sleep;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.bakalaurinis_v2.MainWindowActivity;
import com.example.bakalaurinis_v2.R;

public class SleepBroadcastReceiver extends BroadcastReceiver {

    private String id;

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent newIntent = new Intent(context, MainWindowActivity.class);
        newIntent.putExtra("id", "2");
        newIntent.putExtra("fragment", "sleep");
        newIntent.putExtra("clockAlert", "true");

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.waterfall);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyClock")
                .setSmallIcon(R.drawable.ic_local_hotel_black_24dp)
                .setContentTitle("Žadintuvas")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(sound)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(200, builder.build());

    }
}
