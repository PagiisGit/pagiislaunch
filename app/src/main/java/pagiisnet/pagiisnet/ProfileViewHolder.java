package pagiisnet.pagiisnet;

import com.google.firebase.database.Exclude;

public class ProfileViewHolder {

    private String userName;
    private String userFullname;
    private String adminStatus;
    private String emailAdress;
    private String  mKey;
    private String  proffessionStatus;
    private String  userMobileNumber;
    private String  userStatus;
    private String  userImageProfilePic;

    private String userAge;
    private  String userSocialLink;
    private String PostTime;


    public ProfileViewHolder() {
        //empty constructor needed for Firebase
    }




    public ProfileViewHolder(String userNameAsEmail, String full_name, String admin, String email_address,String Proffession,String mobile_number, String userDefaultStatus,String userImageDp,String Age,String socialLink) {
        if (userNameAsEmail.trim().equals("")) {
            userNameAsEmail = "No Name";
        }

        userName = userNameAsEmail;
        userFullname = full_name;
        adminStatus = admin;
        emailAdress = email_address;

        proffessionStatus = Proffession;
        userMobileNumber = mobile_number;
        userStatus = userDefaultStatus;
        userImageProfilePic = userImageDp;

        userAge = Age;
        userSocialLink =socialLink;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFullname() {
        return userFullname;
    }

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    public String getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(String adminStatus) {
        this.adminStatus = adminStatus;
    }

    public String getEmailAdress() {
        return emailAdress;
    }

    public void setEmailAdress(String emailAdress) {
        this.emailAdress = emailAdress;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getProffessionStatus() {
        return proffessionStatus;
    }

    public void setProffessionStatus(String proffessionStatus) {
        this.proffessionStatus = proffessionStatus;
    }

    public String getUserMobileNumber() {
        return userMobileNumber;
    }

    public void setUserMobileNumber(String userMobileNumber) {
        this.userMobileNumber = userMobileNumber;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserImageProfilePic() {
        return userImageProfilePic;
    }

    public void setUserImageProfilePic(String userImageDp) {
        this.userImageProfilePic = userImageDp;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserSocialLink() {
        return userSocialLink;
    }

    public void setUserSocialLink(String userSocialLink) {
        this.userSocialLink = userSocialLink;
    }

    public String getPostTime() {
        return PostTime;
    }

    public void setPostTime(String postTime) {
        PostTime = postTime;
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
