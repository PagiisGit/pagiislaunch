package pagiisnet.pagiisnet;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationPermissionHelper {

    public static void checkAndRequestNotificationPermission(Context context) {
        // Check if the app is running on Android 13 (API level 33) or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Check if notification permission is granted
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (!notificationManager.areNotificationsEnabled()) {
                // Show a toast and prompt the user to enable notifications manually
                Toast.makeText(context, "Please enable notifications in settings", Toast.LENGTH_LONG).show();
                openNotificationSettings(context);
            }
        } else {
            // For devices below Android 13, notifications are automatically granted
            Toast.makeText(context, "Notifications enabled", Toast.LENGTH_SHORT).show();
        }
    }

    // Open app settings to allow users to manually enable notifications
    private static void openNotificationSettings(Context context) {
        try {
            // Open the app's notification settings page
            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, context.getPackageName());
            context.startActivity(intent);
        } catch (Exception e) {
            // If the above method fails, open the general settings page
            Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
            context.startActivity(intent);
        }
    }

    // Method to send a test notification (optional)
    public static void sendTestNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.drawable.pagiis_splash_logo_edit_final_file)
                .setContentTitle("Pagiis welcome message")
                .setContentText("Hi, Welcome to Pagiis. ")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }
}
