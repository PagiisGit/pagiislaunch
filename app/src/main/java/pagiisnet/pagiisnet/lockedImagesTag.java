package pagiisnet.pagiisnet;

public class lockedImagesTag {

    private String user_tagID;
    private String user_tagIDx;


    public lockedImagesTag() {
        //empty constructor needed for Firebase
    }

    public lockedImagesTag(String user_tagID, String user_tagIDx) {
        this.user_tagID = user_tagID;
        this.user_tagIDx = user_tagIDx;
    }

    public String getUser_tagIDx() {
        return user_tagIDx;
    }

    public void setUser_tagIDx(String user_tagIDx) {
        this.user_tagIDx = user_tagIDx;
    }

    public String getUser_tagID() {
        return user_tagID;
    }

    public void setUser_tagID(String user_tagID) {
        this.user_tagID = user_tagID;
    }
}
