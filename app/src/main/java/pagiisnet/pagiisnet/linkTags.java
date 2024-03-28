package pagiisnet.pagiisnet;

public class linkTags {
    private String facebookLinkitSet;
    private String twitterLinkSet;
    private String instagramLinkSet;


    public linkTags() {
        //empty constructor needed for Firebase
    }

    public linkTags(String user_tagID, String FacebookLinkitSet, String TwitterLinkSet,String InstagramLinkSet) {
        this.facebookLinkitSet = FacebookLinkitSet;
        this.twitterLinkSet = TwitterLinkSet;
        this.instagramLinkSet = InstagramLinkSet;
    }


    public String getFacebookLinkitSet() {
        return facebookLinkitSet;
    }

    public void setFacebookLinkitSet(String facebookLinkitSet) {
        this.facebookLinkitSet = facebookLinkitSet;
    }

    public String getTwitterLinkSet() {
        return twitterLinkSet;
    }

    public void setTwitterLinkSet(String twitterLinkSet) {
        this.twitterLinkSet = twitterLinkSet;
    }

    public String getInstagramLinkSet() {
        return instagramLinkSet;
    }

    public void setInstagramLinkSet(String instagramLinkSet) {
        this.instagramLinkSet = instagramLinkSet;
    }
}
