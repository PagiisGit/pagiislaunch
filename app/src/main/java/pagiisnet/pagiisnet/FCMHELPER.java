package pagiisnet.pagiisnet;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FCMHELPER {

    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "AAAA64f0YOg:APA91bF98RhT-3jKt1zHvDbhzdlYWGVqX_NqitzwMp_3SpC8ZgIoantI0GfhUKDJ6qLUBZcXpdn857BzkyKhIYAFM6eX0pT3Zmxkut08HBj1mABJPzjqsEWzg2pf9Q8F9hL5l2qWH-sC";  // Replace with your FCM server key

    public static void sendPushNotification(String deviceToken, String title, String message) {
        new Thread(() -> {
            try {
                URL url = new URL(FCM_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "key=" + SERVER_KEY);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Prepare the payload
                String payload = "{"
                        + "\"to\": \"" + deviceToken + "\","
                        + "\"notification\": {"
                        + "\"title\": \"" + title + "\","
                        + "\"body\": \"" + message + "\""
                        + "}"
                        + "}";

                // Send the request
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = payload.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Get the response
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    System.out.println("Notification sent successfully!");
                } else {
                    System.out.println("Failed to send notification. Response code: " + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}

