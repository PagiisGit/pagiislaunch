package pagiisnet.pagiisnet;

public class friendRequests {

    private String userImageDp;
    private  String userNameAsEmail;
    private String userDefaultStatus;
    private String userThumbImageDp;


    public friendRequests() {
    }


    public friendRequests(String userImageDp, String userNameAsEmail, String userDefaultStatus, String userThumbImageDp) {
        this.userImageDp = userImageDp;
        this.userNameAsEmail = userNameAsEmail;
        this.userDefaultStatus = userDefaultStatus;
        this.userThumbImageDp = userThumbImageDp;
    }


    public String getUserImageDp() {
        return userImageDp;
    }

    public void setUserImageDp(String userImageDp) {
        this.userImageDp = userImageDp;
    }

    public String getUserNameAsEmail() {
        return userNameAsEmail;
    }

    public void setUserNameAsEmail(String userNameAsEmail) {
        this.userNameAsEmail = userNameAsEmail;
    }

    public String getUserDefaultStatus() {
        return userDefaultStatus;
    }

    public void setUserDefaultStatus(String userDefaultStatus) {
        this.userDefaultStatus = userDefaultStatus;
    }

    public String getUserThumbImageDp() {
        return userThumbImageDp;
    }

    public void setUserThumbImageDp(String userThumbImageDp) {
        this.userThumbImageDp = userThumbImageDp;
    }
}
