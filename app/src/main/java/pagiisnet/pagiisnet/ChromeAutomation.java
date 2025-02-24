package pagiisnet.pagiisnet;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;

public class ChromeAutomation {
    private static final int TOTAL_REQUESTS = 100; // Open links 100 times
    private static final int DELAY_BETWEEN_REQUESTS = 5000; // 5-second delay between opens
    private static final int RESET_DELAY = 60000; // 1-minute delay before restarting
    private static int requestCount = 0;
    private static Handler handler = new Handler();
    private static String url = "https://pagiis.co.za"; // Change this to your target URL

    public static void startAutomation(Context context) {
        requestCount = 0; // Reset request count

        Runnable openLinkTask = new Runnable() {
            @Override
            public void run() {
                if (requestCount < TOTAL_REQUESTS) {
                    openInChrome(context, url); // Open link in Chrome
                    requestCount++;

                    // Schedule the next opening after a delay
                    handler.postDelayed(this, DELAY_BETWEEN_REQUESTS);
                } else {
                    // After 100 requests, wait 1 minute, then restart
                    handler.postDelayed(() -> startAutomation(context), RESET_DELAY);
                }
            }
        };

        handler.post(openLinkTask); // Start the process
    }

    private static void openInChrome(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setPackage("com.android.chrome");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
