package dev.ade.project.pojo;

import dev.ade.project.annotations.ColumnName;
import dev.ade.project.annotations.PrimaryKey;
import dev.ade.project.annotations.TableName;

import java.util.Objects;

@TableName(key = "post")
public class PostPojo {

    @ColumnName(key = "post_id")
    private int postId;

    @PrimaryKey
    @ColumnName(key = "user_id")
    private int userId;

    @ColumnName(key = "title")
    private String title;

    @ColumnName(key = "country")
    private String country;

    @ColumnName(key = "city")
    private String city;

    @ColumnName(key = "tag")
    private String tag;

    @ColumnName(key = "rating")
    private int rating;

    public PostPojo(){}

    public PostPojo(int postId, int userId, String title, String country, String city, String tag, int rating){
        super();
        this.postId = postId;
        this.userId = userId;
        this.title = title;
        this.country = country;
        this.city = city;
        this.tag = tag;
        this.rating = rating;
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
        return postId == postPojo.postId && userId == postPojo.userId && rating == postPojo.rating && Objects.equals(title, postPojo.title) && Objects.equals(country, postPojo.country) && Objects.equals(city, postPojo.city) && Objects.equals(tag, postPojo.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, userId, title, country, city, tag, rating);
    }

    @Override
    public String toString() {
        return "PostPojo{" +
                "postId=" + postId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", tag='" + tag + '\'' +
                ", rating=" + rating +
                '}';
    }
}