package pagiisnet.pagiisnet;

import android.location.Geocoder;

import com.google.firebase.database.Exclude;

public class createSellItem {



    private String imageName;
    private String imageImageUrl;
    private String exRating;
    private String UserId;
    private String ModelName;
    private String StoreCellNumber;
    private String StoreBankDetails;
    private String  mKey;

    private String  WelcomeNote;




    private String  Streams;

    private Geocoder geocoder;

    private String  Likes;
    private String  Ripples;

    private String  Attendance;

    private String Posts;

    private String InviteName;
    private  String InviteDpUrl;

    private String Location;

    public createSellItem() {
        //empty constructor needed for Firebase
    }

    public createSellItem(String nameShop, String imageUrlSHopICon, String storeType, String storeAdminId, String emailStoreAddress,String storeCell,String storebankDetails,String welcomeNote,String streams ,String likes, String ripples, String attendance,String posts,String location,String inviteImageUrl,String inviteName) {
        if (nameShop.trim().equals("")) {
            nameShop = "No Name";
            emailStoreAddress = "Promo";
        }

        UserId = storeAdminId;
        imageName = nameShop;
        exRating = storeType;
        imageImageUrl= imageUrlSHopICon;
        ModelName = emailStoreAddress;
        StoreBankDetails = storebankDetails;
        StoreCellNumber = storeCell;


        Likes = likes;
        Ripples =ripples;
        WelcomeNote = welcomeNote;
        Streams = streams;
        Posts = posts;
        Attendance = attendance;

        InviteName = inviteName;

        InviteDpUrl = inviteImageUrl;


        Location = location;


    }

    public String getWelcomeNote() {
        return WelcomeNote;
    }

    public void setWelcomeNote(String welcomeNote) {
        WelcomeNote = welcomeNote;
    }

    public String getStreams() {
        return Streams;
    }

    public void setStreams(String streams) {
        Streams = streams;
    }

    public String getLikes() {
        return Likes;
    }

    public void setLikes(String likes) {
        Likes = likes;
    }

    public String getRipples() {
        return Ripples;
    }

    public void setRipples(String ripples) {
        Ripples = ripples;
    }

    public String getAttendance() {
        return Attendance;
    }

    public void setAttendance(String attendance) {
        Attendance = attendance;
    }

    public String getPosts() {
        return Posts;
    }

    public void setPosts(String posts) {
        Posts = posts;
    }

    public String getInviteName() {
        return InviteName;
    }

    public void setInviteName(String inviteName) {
        InviteName = inviteName;
    }

    public String getInviteDpUrl() {
        return InviteDpUrl;
    }

    public void setInviteDpUrl(String inviteDpUrl) {
        InviteDpUrl = inviteDpUrl;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getStoreCellNumber()
    {
        return StoreCellNumber;
    }

    public void setStoreCellNumber(String storeCellNumber) {
        StoreCellNumber = storeCellNumber;
    }

    public String getStoreBankDetails() {
        return StoreBankDetails;
    }

    public void setStoreBankDetails(String storeBankDetails) {
        StoreBankDetails = storeBankDetails;
    }

    public String getModelName() {
        return ModelName;
    }

    public void setModelName(String modelName) {
        ModelName = modelName;
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
