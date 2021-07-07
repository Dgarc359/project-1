package dev.ade.project.pojo;

import java.util.Objects;

public class UserPojo {

    private int user_id;
    private String first_name;
    private String last_name;
    private char gender;
    private String username;
    private String user_password;

    UserPojo(){}

    UserPojo(int user_id, String first_name, String last_name, char gender, String username, String user_password){
        super();
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.username = username;
        this.user_password = user_password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
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
        return user_id == userPojo.user_id && gender == userPojo.gender && Objects.equals(first_name, userPojo.first_name) && Objects.equals(last_name, userPojo.last_name) && Objects.equals(username, userPojo.username) && Objects.equals(user_password, userPojo.user_password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, first_name, last_name, gender, username, user_password);
    }

    @Override
    public String toString() {
        return "UserPojo{" +
                "user_id=" + user_id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", gender=" + gender +
                ", username='" + username + '\'' +
                ", user_password='" + user_password + '\'' +
                '}';
    }
}
