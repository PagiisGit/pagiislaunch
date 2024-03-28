package pagiisnet.pagiisnet;

public class RadiusUsers {

    private  String user_name;
    private String Status;
    private String user_image;

    public RadiusUsers() {
    }

    public RadiusUsers(String user_name, String status, String user_image) {
        this.user_name = user_name;
        this.Status = Status;
        this.user_image = user_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }
}
