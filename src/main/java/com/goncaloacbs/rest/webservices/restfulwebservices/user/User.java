package com.goncaloacbs.rest.webservices.restfulwebservices.user;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User {
    private Integer id;

    @Size(min = 2, message = "Name should have at least 2 characters")
    private String name;

    @Past
    private Date birthDate;
    private Map<Integer, Post> posts;

    public User(Integer id, String name, Date birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.posts = new HashMap<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Collection<Post> getPosts() {
        return posts.values();
    }

    public Post createPost(Post post) {
        Integer id = posts.size();
        post.setId(id);
        posts.put(id, post);
        return post;
    }

    public Post getPost(Integer id) {
        if (posts.containsKey(id)) {
            return posts.get(id);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
