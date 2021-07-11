package dev.ade.project.pojo;

import dev.ade.project.annotations.*;

import java.util.Objects;

@TableName(tableName = "users")
public class User {

    @PrimaryKey
    @ColumnName(columnName = "user_id")
    private int userId;

    @ColumnName(columnName = "first_name")
    private String firstName;

    @ColumnName(columnName = "last_name")
    private String lastName;

    @ColumnName(columnName = "gender")
    private char gender;

    @Unique
    @ColumnName(columnName = "username")
    private String username;

    @ColumnName(columnName = "user_password")
    private String userPassword;

    public User(){}

    public User(int userId, String firstName, String lastName, char gender, String username, String userPassword){
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
        User user = (User) o;
        return userId == user.userId && gender == user.gender && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(username, user.username) && Objects.equals(userPassword, user.userPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, firstName, lastName, gender, username, userPassword);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", username='" + username + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }
}
