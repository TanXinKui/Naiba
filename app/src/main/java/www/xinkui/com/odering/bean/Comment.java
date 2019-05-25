package www.xinkui.com.odering.bean;

import java.sql.Date;

public class Comment {
    private String comment;
    private String time;
    private String username;
    private int sex;

    public Comment(String comment, String time, String username, int sex) {
        this.comment = comment;
        this.time = time;
        this.username = username;
        this.sex = sex;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
