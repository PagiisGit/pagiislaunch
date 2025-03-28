package pagiisnet.pagiisnet;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Collections;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCMService";
    private static final String CHANNEL_ID = "Pagiis_notifications";
    private static final String CHANNEL_NAME = "Pagiis Notifications";
    private static final String CHANNEL_DESCRIPTION = "Notifications for Pagiis updates";

    private static final String FCM_API_URL = "https://fcm.googleapis.com/v1/projects/YOUR_PROJECT_ID/messages:send"; // Use your actual Firebase Project ID

    @SuppressLint("NewApi")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "FCM Message received from: " + remoteMessage.getFrom());

        String title = "New Notification";
        String messageBody = "You have a new message";

        // Handle Data Payload
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "FCM Data Payload: " + remoteMessage.getData());
            title = remoteMessage.getData().getOrDefault("title", title);
            messageBody = remoteMessage.getData().getOrDefault("message", messageBody);
        }

        // Handle Notification Payload (if available)
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "FCM Notification Body: " + remoteMessage.getNotification().getBody());
            if (remoteMessage.getNotification().getTitle() != null) {
                title = remoteMessage.getNotification().getTitle();
            }
            if (remoteMessage.getNotification().getBody() != null) {
                messageBody = remoteMessage.getNotification().getBody();
            }
        }

        sendNotification(title, messageBody);
    }

    private void sendNotification(String title, String messageBody) {
        Context context = getApplicationContext();

        // Wake up device when notification arrives
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK, "MyApp::NotificationWakelock");
        try {
            wakeLock.acquire(3000); // Keep device awake for 3 seconds

            // Intent to open MainActivity when notification is clicked
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            int pendingIntentFlags = PendingIntent.FLAG_UPDATE_CURRENT;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntentFlags |= PendingIntent.FLAG_MUTABLE;
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(
                    this, 0, intent, pendingIntentFlags
            );

            // Notification sound
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // Build notification
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.pagiis_splash_logo_edit_final_file)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.pagiis_splash_logo_edit_final_file))
                            .setContentTitle(title)
                            .setContentText(messageBody)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody)) // Expandable notification
                            .setAutoCancel(true)
                            .setSound(soundUri)
                            .setContentIntent(pendingIntent)
                            .setPriority(NotificationCompat.PRIORITY_HIGH);

            // Get NotificationManager
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // Create Notification Channel for Android 8.0+ (Oreo and above)


            // Create Notification Channel for Android 8.0+ (Oreo and above)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
                );
                channel.setDescription(CHANNEL_DESCRIPTION);
                notificationManager.createNotificationChannel(channel);
            }

            // Show notification
            notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());

        } finally {
            // Ensure WakeLock is always released
            if (wakeLock.isHeld()) {
                wakeLock.release();
            }
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "FCM New Token: " + token);
        sendNewUserToken(token);
    }

    private void sendNewUserToken(String token) {
        new Thread(() -> {
            try {
                Log.d(TAG, "Sending FCM token to server: " + token);

                // Load credentials from your service account (use JSON credentials file)
                GoogleCredentials credentials = GoogleCredentials.fromStream(getAssets().open("service-account.json"))
                        .createScoped(Collections.singletonList("https://www.googleapis.com/auth/cloud-platform"));
                credentials.refreshIfExpired();

                // Obtain the access token
                String accessToken = credentials.getAccessToken().getTokenValue();

                // Construct the payload
                JSONObject message = new JSONObject();
                JSONObject notification = new JSONObject();
                notification.put("title", "FCM Notification Title");
                notification.put("body", "FCM Notification Body");

                JSONObject data = new JSONObject();
                data.put("customData", "12345"); // Optional additional data

                message.put("token", token);
                message.put("notification", notification);
                message.put("data", data);

                JSONObject jsonPayload = new JSONObject();
                jsonPayload.put("message", message);

                // Send HTTP request to FCM HTTP v1 API
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(jsonPayload.toString(), okhttp3.MediaType.get("application/json"));
                Request request = new Request.Builder()
                        .url(FCM_API_URL)
                        .post(body)
                        .addHeader("Authorization", "Bearer " + accessToken)
                        .addHeader("Content-Type", "application/json")
                        .build();

                // Execute the request and handle the response
                try (Response response = client.newCall(request).execute()) {
                    Log.d(TAG, "FCM Response: " + response.body().string());
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception sending token: " + e.getMessage(), e);
            }
        }).start();
    }
}
