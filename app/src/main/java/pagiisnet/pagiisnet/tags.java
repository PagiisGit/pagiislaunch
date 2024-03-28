package pagiisnet.pagiisnet;

public class tags {

    private String user_tagID;


    public tags() {
        //empty constructor needed for Firebase
    }

    public tags(String user_tagID) {
        this.user_tagID = user_tagID;
    }

    public String getUser_tagID() {
        return user_tagID;
    }

    public void setUser_tagID(String user_tagID) {
        this.user_tagID = user_tagID;
    }
}
