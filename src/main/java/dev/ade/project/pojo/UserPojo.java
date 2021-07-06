package dev.ade.project.pojo;

import java.util.Objects;

public class UserPojo {

    private int user_id;
    private String username;
    private String user_password;

    UserPojo(){}

    UserPojo(int user_id, String username, String user_password){
        super();
        this.user_id = user_id;
        this.username = username;
        this.user_password = user_password;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPojo userPojo = (UserPojo) o;
        return user_id == userPojo.user_id && Objects.equals(username, userPojo.username) && Objects.equals(user_password, userPojo.user_password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, username, user_password);
    }

    @Override
    public String toString() {
        return "UserPojo{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", user_password='" + user_password + '\'' +
                '}';
    }
}
