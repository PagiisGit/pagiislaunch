package pagiisnet.pagiisnet;

public class OnlineUserList {


    private static  String email,status;

    public OnlineUserList() {
    }


    public OnlineUserList(String email, String status) {
        OnlineUserList.email = email;
        OnlineUserList.status = status;
    }


    public static String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        OnlineUserList.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        OnlineUserList.status = status;
    }
}
