package dev.ade.project.pojo;

import java.util.Objects;

public class UserPojo {

    private int userId;
    private String firstName;
    private String lastName;
    private char gender;
    private String username;
    private String userPassword;

    UserPojo(){}

    UserPojo(int userId, String firstName, String lastName, char gender, String username, String userPassword){
        super();
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.username = username;
        this.userPassword = userPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPojo userPojo = (UserPojo) o;
        return userId == userPojo.userId && gender == userPojo.gender && Objects.equals(firstName, userPojo.firstName) && Objects.equals(lastName, userPojo.lastName) && Objects.equals(username, userPojo.username) && Objects.equals(userPassword, userPojo.userPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, firstName, lastName, gender, username, userPassword);
    }

    @Override
    public String toString() {
        return "UserPojo{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", username='" + username + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }
}
