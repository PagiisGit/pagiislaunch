package pagiisnet.pagiisnet.Utils;

import android.util.Log;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONObject;

import java.util.Collections;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyFirebaseIdService extends FirebaseMessagingService {

    private static final String TAG = "FCMService";
    private static final String FCM_API_URL = "https://fcm.googleapis.com/v1/projects/pagiis-ix/messages:send"; // Replace with your actual project ID

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        sendNewTokenToServer(token);
    }

    private void sendNewTokenToServer(String token) {
        new Thread(() -> {
            try {
                Log.d(TAG, "Sending FCM token to server: " + token);

                // Securely retrieve credentials from your backend (important to move Firebase Admin SDK usage to the server)
                GoogleCredentials credentials = GoogleCredentials.fromStream(getAssets().open("pagiis-ix-firebase-adminsdk-grvv2-0c39556ec0.json"))
                        .createScoped(Collections.singletonList("https://www.googleapis.com/auth/cloud-platform"));
                credentials.refreshIfExpired();

                // Obtain the access token from the credentials
                String accessToken = credentials.getAccessToken().getTokenValue();

                // Construct the notification message
                JSONObject message = new JSONObject();
                JSONObject notification = new JSONObject();
                notification.put("title", "FCM Notification Title");
                notification.put("body", "FCM Notification Body");

                // Custom data (optional)
                JSONObject data = new JSONObject();
                data.put("customData", "12345");

                // Combine the message, notification, and data
                message.put("token", token);
                message.put("notification", notification);
                message.put("data", data);

                // Wrap everything in the payload
                JSONObject jsonPayload = new JSONObject();
                jsonPayload.put("message", message);

                // Prepare OkHttp client and request
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
                    if (response.isSuccessful()) {
                        Log.d(TAG, "FCM token sent successfully: " + response.body().string());
                    } else {
                        Log.e(TAG, "Failed to send token. Response code: " + response.code() + ", Message: " + response.body().string());
                    }
                }

            } catch (Exception e) {
                Log.e(TAG, "Exception while sending token: " + e.getMessage(), e);
            }
        }).start();
    }
}
