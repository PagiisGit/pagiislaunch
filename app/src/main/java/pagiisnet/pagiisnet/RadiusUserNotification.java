package pagiisnet.pagiisnet;

import com.google.firebase.database.Exclude;

public class RadiusUserNotification {
    private String imageName;
    private String imageImageUrl;
    private String exRating;
    private String UserId;
    private String  mKey;

    public RadiusUserNotification() {
        //empty constructor needed for Firebase
    }

    public RadiusUserNotification(String name, String imageUrl, String rateEx, String userId) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        UserId = userId ;
        imageName = name;
        exRating = rateEx;
        imageImageUrl= imageUrl;
    }


    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getExRating() {
        return exRating;
    }

    public void setExRating(String exRating) {
        this.exRating = exRating;
    }

    public String getName() {
        return imageName;
    }

    public void setName(String name) {
        imageName = name;
    }

    public String getImageUrl() {
        return imageImageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        imageImageUrl = imageUrl;
    }


    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String Key) {
        mKey = Key;
    }
}
