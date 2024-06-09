package pagiisnet.pagiisnet;

import android.app.Activity;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FcmNotificationsSender {

    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey = "AAAA64f0YOg:APA91bF98RhT-3jKt1zHvDbhzdlYWGVqX_NqitzwMp_3SpC8ZgIoantI0GfhUKDJ6qLUBZcXpdn857BzkyKhIYAFM6eX0pT3Zmxkut08HBj1mABJPzjqsEWzg2pf9Q8F9hL5l2qWH-sC";
    String userFcmToken;
    String title;
    String body;
    String userId;
    Context mContext;
    Activity mActivity;
    private RequestQueue requestQueue;


    public FcmNotificationsSender(String userFcmToken, String title, String body, Context mContext, Activity mActivity,String userIf) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
        this.mActivity = mActivity;


    }

    public void SendNotifications() {

        requestQueue = Volley.newRequestQueue(mActivity);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", userFcmToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title", title);
            notiObject.put("body", body);
            notiObject.put("noticeId",userId);
            notiObject.put("Pagiis Notifcation", R.drawable.pagiis_splash_logo_edit_final_file); // enter icon that exists in drawable only


            mainObj.put("Pagiis notification", notiObject);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    // code run is got response

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // code run is got error

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {


                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + fcmServerKey);
                    return header;


                }
            };
            requestQueue.add(request);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
