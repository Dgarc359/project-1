package dev.ade.project.pojo;

import java.sql.Timestamp;
import java.util.Objects;

public class PostPojo {

    private String tableName = "post";
    private int postId;
    private int userId;
    private String title;
    private String country;
    private String city;
    private String tag;
    private int rating;
    private Timestamp timestamp;

    public PostPojo(){}

    public PostPojo(int postId, int userId, String title, String country, String city, String tag, int rating, Timestamp timestamp){
        super();
        this.postId = postId;
        this.userId = userId;
        this.title = title;
        this.country = country;
        this.city = city;
        this.tag = tag;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostPojo postPojo = (PostPojo) o;
        return postId == postPojo.postId && userId == postPojo.userId && rating == postPojo.rating && Objects.equals(tableName, postPojo.tableName) && Objects.equals(title, postPojo.title) && Objects.equals(country, postPojo.country) && Objects.equals(city, postPojo.city) && Objects.equals(tag, postPojo.tag) && Objects.equals(timestamp, postPojo.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName, postId, userId, title, country, city, tag, rating, timestamp);
    }

    @Override
    public String toString() {
        return "PostPojo{" +
                "tableName='" + tableName + '\'' +
                ", postId=" + postId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", tag='" + tag + '\'' +
                ", rating=" + rating +
                ", timestamp=" + timestamp +
                '}';
    }
}
