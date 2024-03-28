package pagiisnet.pagiisnet.Utils;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        sendNewTokenToSever(FirebaseInstanceId.getInstance().getToken());
    }

    private void sendNewTokenToSever(String token)
    {
    }
}
