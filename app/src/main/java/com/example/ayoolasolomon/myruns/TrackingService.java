package com.example.ayoolasolomon.myruns;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;

public class TrackingService extends Service {

  final static String ACTION = "NotifyServiceAction";
  final static String STOP_SERVICE_BROADCAST_KEY="StopServiceBroadcastKey";
  final static int RQS_STOP_SERVICE = 1;

  NotifyServiceReceiver notifyServiceReceiver;

  @Override
  public void onCreate() {
    notifyServiceReceiver = new NotifyServiceReceiver();
    super.onCreate();
  }

  public TrackingService() {
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {

    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(ACTION);
    registerReceiver(notifyServiceReceiver, intentFilter);

    String text = getString(R.string.recodr_path);
    Intent i = new Intent(this, MapDisplayActivity.class);
    i.addCategory(Intent.CATEGORY_LAUNCHER);
    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

    PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, i, 0);

    Notification notification = new Notification.Builder(this)
        .setContentTitle(getString(R.string.app_name))
        .setContentText(text)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentIntent(pendingIntent)
        .build();

    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
    notification.flags |= Notification.FLAG_AUTO_CANCEL;

    notificationManager.notify(0, notification);
    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public void onDestroy() {
    this.unregisterReceiver(notifyServiceReceiver);
    super.onDestroy();
  }

  @Override
  public IBinder onBind(Intent intent) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public class NotifyServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      int rqs = intent.getIntExtra(STOP_SERVICE_BROADCAST_KEY, 0);

      if (rqs == RQS_STOP_SERVICE) {
        stopSelf();
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
            .cancelAll();
      }
    }
  }
}
