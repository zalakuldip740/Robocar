package com.example.roboticcar;
 import android.app.Notification;
 import android.app.NotificationManager;
 import android.app.PendingIntent;
        import android.app.Service;
        import android.content.Intent;
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

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(datamsg)
                .setContentText(datetimemsg)
                .setSmallIcon(R.drawable.robocaricon)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
        stopSelf();

        NotificationManager manager = getSystemService(NotificationManager.class);
       manager.notify(1, notification);
        startForeground(1, notification);




        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}