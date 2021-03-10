package com.example.roboticcar;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.example.roboticcar.App.CHANNEL_ID;


public class exservice extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String datamsg = intent.getStringExtra("datamsg");
        String datetimemsg = intent.getStringExtra("datetimemsg");

        Intent notificationIntent = new Intent(this, Splashscreen.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);


        Bitmap largeicon=BitmapFactory.decodeResource(getResources(), R.drawable.robocaricon);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Latest Alert : " + datamsg)
                .setContentText("Time : " + datetimemsg)
                .setSmallIcon(R.drawable.robocaricon)
                .setLargeIcon(largeicon)
                .setContentIntent(pendingIntent)
                .setShowWhen(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        startForeground(1, notification);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}