package pagiisnet.pagiisnet.Utils;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFirebaseIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        sendNewTokenToSever(FirebaseInstanceId.getInstance().getToken());
    }

    private void sendNewTokenToSever(String token)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Construct URL to your server endpoint
                    URL url = new URL("YOUR_SERVER_ENDPOINT");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    // Write token to the request body
                    OutputStream os = conn.getOutputStream();
                    os.write(token.getBytes());
                    os.flush();
                    os.close();

                    // Get the response code
                    int responseCode = conn.getResponseCode();
                    // Handle the response code appropriately

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // The request was successful
                        Log.d("FCM Token", "Token sent successfully");
                    } else {
                        // Handle other response codes (e.g., error handling)
                        Log.e("FCM Token", "Failed to send token. Response code: " + responseCode);
                    }

                    // Close the connection
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
