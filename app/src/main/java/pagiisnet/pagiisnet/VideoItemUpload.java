package pagiisnet.pagiisnet;

import com.google.firebase.database.Exclude;

public class VideoItemUpload {

    private String ImageName;
    private String imageImageUrl;
    private String exRating;
    private String UserId;
    private String  mKey;

    private String MyName;
    private String TimePost;
    private String PostLocation;

    private  String Likes;
    private  String Views;
    private  String S;

    public VideoItemUpload()
    {


    }
    public VideoItemUpload(String name, String imageUrl, String rateEx, String userId,String myName,String timePost,String postLocation,String likes,String views,String s) {
        if (name.trim().equals(""))
        {
            name = "No Name";
        }

        UserId = userId ;
        ImageName = name;
        exRating = rateEx;
        imageImageUrl= imageUrl;
        MyName = myName;
        TimePost = timePost;
        PostLocation = postLocation;

        Likes = likes;
        Views = views;
        S= s;


    }


    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        this.ImageName = imageName;
    }

    public String getLikes() {
        return Likes;
    }

    public void setLikes(String likes) {
        Likes = likes;
    }

    public String getViews() {
        return Views;
    }

    public void setViews(String views) {
        Views = views;
    }

    public String getS() {
        return S;
    }

    public void setS(String s) {
        S = s;
    }

    public String getMyName() {
        return MyName;
    }

    public void setMyName(String myName) {
        MyName = myName;
    }

    public String getTimePost() {
        return TimePost;
    }

    public void setTimePost(String timePost) {
        TimePost = timePost;
    }

    public String getPostLocation() {
        return PostLocation;
    }

    public void setPostLocation(String postLocation)
    {
        PostLocation = postLocation;
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
        return ImageName;
    }

    public void setName(String name) {
        ImageName = name;
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
