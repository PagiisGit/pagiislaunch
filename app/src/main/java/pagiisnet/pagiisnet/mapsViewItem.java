package pagiisnet.pagiisnet;

public class mapsViewItem {

    private String User_tagID;

    private String User_ImageUrl;


    public mapsViewItem() {
        //empty constructor needed for Firebase
    }

    public mapsViewItem(String user_tagID, String user_ImageUrl) {
        this.User_tagID = user_tagID;

        this.User_ImageUrl =user_ImageUrl;
    }

    public String getUser_ImageUrl() {
        return User_ImageUrl;
    }

    public void setUser_ImageUrl(String user_ImageUrl) {
        User_ImageUrl = user_ImageUrl;
    }

    public String getUser_tagID() {
        return User_tagID;
    }

    public void setUser_tagID(String user_tagID) {
        this.User_tagID = user_tagID;
    }
}
