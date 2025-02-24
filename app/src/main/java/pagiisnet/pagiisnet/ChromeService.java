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
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ChromeService extends Service {
    private static final int TOTAL_REQUESTS = 100;
    private static final int DELAY_BETWEEN_REQUESTS = 5000; // 5 sec delay
    private static final int RESET_DELAY = 60000; // 1 min delay before restarting
    private static int requestCount = 0;
    private static final int SERVICE_NOTIFICATION_ID = 1;
    private Handler handler = new Handler();
    private String url = "https://pagiis.co.za"; // Replace with your target URL

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        startForegroundService();
        startAutomation();
        return START_STICKY; // Ensures service restarts if killed
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

        Intent intent = new Intent(this, ChromeService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, "TorServiceChannel")
                .setContentTitle("Chrome Automation Running")
                .setContentText("Process is active in the background.")
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your app's icon
                .setContentIntent(pendingIntent)
                .build();

        startForeground(SERVICE_NOTIFICATION_ID, notification);
    }

    private void startAutomation() {
        requestCount = 0; // Reset count

        Runnable openLinkTask = new Runnable() {
            @Override
            public void run() {
                if (requestCount < TOTAL_REQUESTS) {
                    changeIPAddress(); // Step 1: Change IP
                    handler.postDelayed(() -> {
                        openInChrome(getApplicationContext(), url); // Step 2: Open link
                        requestCount++;

                        // Step 3: Repeat process
                        handler.postDelayed(this, DELAY_BETWEEN_REQUESTS);
                    }, 5000); // Wait for IP change

                } else {
                    // Step 4: Restart process after 1 min
                    handler.postDelayed(() -> startAutomation(), RESET_DELAY);
                }
            }
        };

        handler.post(openLinkTask);
    }

    private void changeIPAddress() {
        // Option 1: Send intent to VPN app (if supported)
        //Intent vpnIntent = new Intent("com.vpnapp.CHANGE_SERVER");
        //vpnIntent.setPackage("com.vpnapp.package"); // Replace with your VPN package name
        //sendBroadcast(vpnIntent);

        // Option 2: Toggle Airplane Mode (for mobile networks)
        try {
            Settings.Global.putInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 1);
            Thread.sleep(3000); // Wait 3 sec
            Settings.Global.putInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void openInChrome(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setPackage("com.android.chrome");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
