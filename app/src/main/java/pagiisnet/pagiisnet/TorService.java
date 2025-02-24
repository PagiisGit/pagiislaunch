package pagiisnet.pagiisnet;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class TorService extends Service {
    private static final int TOTAL_REQUESTS = 100;
    private static final int DELAY_BETWEEN_REQUESTS = 2000; // 2 seconds delay
    private static final int CLOSE_TABS_DELAY = 60000; // 1 minute after all requests
    private static final int SERVICE_NOTIFICATION_ID = 1;
    private static int requestCount = 0;
    private static Handler handler = new Handler();
    private String url = "https://pagiis.co.za"; // Change this to your target URL

    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundService();
        startAutomation();
    }

    private void startForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "TorServiceChannel",
                    "Tor Automation Service",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        Intent intent = new Intent(this, TorService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, "TorServiceChannel")
                .setContentTitle("Tor Automation Running")
                .setContentText("Process is active in the background.")
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your app's icon
                .setContentIntent(pendingIntent)
                .build();

        startForeground(SERVICE_NOTIFICATION_ID, notification);
    }

    private void startAutomation() {
        requestCount = 0; // Reset request count for a fresh cycle

        Runnable openLinkTask = new Runnable() {
            @Override
            public void run() {
                if (requestCount < TOTAL_REQUESTS) {
                    openWithTor(getApplicationContext(), url);  // Step 1: Open link in Tor

                    handler.postDelayed(() -> {
                        resetTorIdentity(getApplicationContext()); // Step 2: Reset identity (clears cookies + new IP)

                        handler.postDelayed(() -> {
                            restartTor(getApplicationContext()); // Step 3: Restart Tor to get a new IP
                            requestCount++;
                            handler.postDelayed(this, DELAY_BETWEEN_REQUESTS); // Step 4: Wait & repeat
                        }, 2000);

                    }, 2000);
                } else {
                    // Step 5: Close all tabs after 1 minute
                    handler.postDelayed(() -> {
                        closeTorBrowser(getApplicationContext());

                        // Step 6: Restart the entire process
                        handler.postDelayed(() -> startAutomation(), 5000);
                        // Restart after 5 seconds

                    }, CLOSE_TABS_DELAY);
                }
            }
        };
        handler.post(openLinkTask);
    }

    private static void openWithTor(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setPackage("org.torproject.torbrowser");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private static void restartTor(Context context) {
        Intent intent = new Intent("org.torproject.android.intent.action.RESTART");
        intent.setPackage("org.torproject.android");
        context.sendBroadcast(intent);
    }

    private static void resetTorIdentity(Context context) {
        Intent intent = new Intent("org.torproject.android.intent.action.NEW_IDENTITY");
        intent.setPackage("org.torproject.android");
        context.sendBroadcast(intent);
    }

    private static void closeTorBrowser(Context context) {
        Intent intent = new Intent("org.torproject.android.intent.action.STOP");
        intent.setPackage("opagiisnet.pagiisnet");
        context.sendBroadcast(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // Ensures service restarts if killed by Android
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null); // Stop the handler when service is destroyed
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

