package com.lipstudio.pnimiyapp;

import java.io.Serializable;

public class Post implements Serializable {

    private String description;
    private String imageLink;
    private long postId;
    private long userId;
    private String urlLink;


    public Post(String description, String imageLink,String urlLink, long userId){
        this.description = description;
        this.imageLink = imageLink;
        this.userId = userId;
        this.urlLink = urlLink;
    }

    public Post(String description, String imageLink,String urlLink, long postId, long userId){
        this.description = description;
        this.imageLink = imageLink;
        this.postId = postId;
        this.userId = userId;
        this.urlLink = urlLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUrlLink() {
        return urlLink;
    }

    public void setUrlLink(String urlLink) {
        this.urlLink = urlLink;
    }

}
