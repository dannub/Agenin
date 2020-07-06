package com.agenin.id.Helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.agenin.id.Activity.MainActivity;
import com.agenin.id.Activity.RegisterActivity;
import com.agenin.id.Fragment.ui.SignUpFragment;
import com.agenin.id.MyFirebaseMessagingService;
import com.agenin.id.R;

import static java.security.AccessController.getContext;

public class NotificationHelper {

    public static NotificationManager notifManager;

    public static void displayNotification(Context context,String title,String text){

        Intent intent = new Intent(context,MainActivity.class);



        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(RegisterActivity.CHANNEL_ID);
            if (mChannel == null) {
                mChannel = new NotificationChannel(RegisterActivity.CHANNEL_ID, title, importance);

                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, RegisterActivity.CHANNEL_ID);
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle("Agenin")                            // required
                    .setSmallIcon(R.drawable.notif_icon)   // required
                    .setContentTitle(title)
                    .setContentText(text)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(text)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        }
        else {
            builder = new NotificationCompat.Builder(context,RegisterActivity.CHANNEL_ID);
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle("Agenin")                             // required
                    .setSmallIcon(R.drawable.notif_icon)   // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(text)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = builder.build();
        notifManager.notify(RegisterActivity.NOTIFY_ID, notification);



//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(context,RegisterActivity.CHANNEL_ID)
//                        .setSmallIcon(R.drawable.icon)
//                        .setContentTitle(title)
//                        .setContentText(text)
//                        .setContentIntent(pendingIntent)
//                        .setAutoCancel(true)
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(context);
//        mNotificationMgr.notify(1,mBuilder.build());
    }
}
