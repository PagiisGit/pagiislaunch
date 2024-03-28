package pagiisnet.pagiisnet;

import com.google.firebase.database.Exclude;

public class ImageUploads {

    private String imageName;
    private String imageImageUrl;
    private String exRating;
    private String UserId;
    private String  mKey;
    private String  Likes;
    private String  Views;
    private String  Chats;
    private String  Share;

    private String PostName;
    private  String PostLocation;
    private String PostTime;


    public ImageUploads() {
        //empty constructor needed for Firebase
    }

    public ImageUploads(String name, String imageUrl, String rateEx, String userId,String views,String likes, String share,String postTime,String postLocation,String postName) {
        if (name != null && name.trim().equals(""))
        {
            name = "No Name";
        }

        UserId = userId ;
        imageName = name;
        exRating = rateEx;
        imageImageUrl= imageUrl;
        Likes= likes;
        Views= views;
        Share= share;
        PostName = postName;
        PostLocation =postLocation;

        PostTime = postTime;


    }

    public String getPostName() {
        return PostName;
    }

    public void setPostName(String postName) {
        PostName = postName;
    }

    public String getPostLocation() {
        return PostLocation;
    }

    public void setPostLocation(String postLocation) {
        PostLocation = postLocation;
    }

    public String getPostTime() {
        return PostTime;
    }

    public void setPostTime(String postTime) {
        PostTime = postTime;
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

    public void setExRating(String exRating)
    {
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



    public String getLikes() {
        return Likes;
    }

    public void setLikes(String likes) {
        this.Likes = likes;
    }

    public String getViews() {
        return Views;
    }

    public void setViews(String views) {
        this.Views = views;
    }

    public String getChats() {
        return Chats;
    }

    public void setChats(String chats) {
        this.Chats = chats;
    }

    public String getShare() {
        return Share;
    }

    public void setShare(String share) {
        this.Share = share;
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
