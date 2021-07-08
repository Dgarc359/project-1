package dev.ade.project.pojo;

import java.sql.Timestamp;
import java.util.Objects;

public class Post {

    private int post_id;
    private int user_id;
    private String title;
    private String country;
    private String city;
    private String tag;
    private int rating;

    public Post(){}

    public Post(int post_id, int user_id, String title, String country, String city, String tag, int rating){
        super();
        this.post_id = post_id;
        this.user_id = user_id;
        this.title = title;
        this.country = country;
        this.city = city;
        this.tag = tag;
        this.rating = rating;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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
        Post post = (Post) o;
        return post_id == post.post_id && user_id == post.user_id && rating == post.rating && Objects.equals(title, post.title) && Objects.equals(country, post.country) && Objects.equals(city, post.city) && Objects.equals(tag, post.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(post_id, user_id, title, country, city, tag, rating);
    }

    @Override
    public String toString() {
        return "Post{" +
                "post_id=" + post_id +
                ", user_id=" + user_id +
                ", title='" + title + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", tag='" + tag + '\'' +
                ", rating=" + rating +
                '}';
    }
}
