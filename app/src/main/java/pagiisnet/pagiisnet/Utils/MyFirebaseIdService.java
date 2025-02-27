package pagiisnet.pagiisnet.Utils;

import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MyFirebaseIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        sendNewTokenToServer(token);
    }

    private void sendNewTokenToServer(String token) {
        new Thread(() -> {
            try {
                // Construct the JSON payload
                JSONObject jsonPayload = new JSONObject();
                jsonPayload.put("fcmToken", token);

                // Construct URL to your server endpoint
                URL url = new URL("https://us-central1-pagiis-ix.cloudfunctions.net/saveFCMToken");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // Send JSON payload
                OutputStream os = conn.getOutputStream();
                os.write(jsonPayload.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

                // Read the response
                int responseCode = conn.getResponseCode();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("FCM Token", "Token sent successfully: " + response.toString());
                } else {
                    Log.e("FCM Token", "Failed to send token. Response code: " + responseCode + ", Message: " + response.toString());
                }

                conn.disconnect();
            } catch (Exception e) {
                Log.e("FCM Token", "Exception: " + e.getMessage(), e);
            }
        }).start();
    }
}
