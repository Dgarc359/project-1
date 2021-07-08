package dev.ade.project.pojo;

import dev.ade.project.annotations.ColumnName;
import dev.ade.project.annotations.PrimaryKey;
import dev.ade.project.annotations.TableName;

@TableName(key = "users")
public class UserPojo {

    @PrimaryKey
    @ColumnName(key = "user_id")
    private int userId;

    @ColumnName(key = "first_name")
    private String firstName;

    @ColumnName(key = "last_name")
    private String lastName;

    @ColumnName(key = "gender")
    private char gender;

    @ColumnName(key = "username")
    private String username;

    @ColumnName(key = "user_password")
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


}
