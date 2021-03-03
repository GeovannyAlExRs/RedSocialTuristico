package com.app.dtk.redsocialturistico.model;

public class Post {

    private String id_post;
    private String title;
    private String description;
    private String reference;
    private String img1;
    private String img2;
    private String category;
    private String idUsers;
    private long timestamp;

    public Post() {
    }

    public Post(String id_post, String title, String description, String reference, String img1, String img2, String category, String idUsers, long timestamp) {
        this.id_post = id_post;
        this.title = title;
        this.description = description;
        this.reference = reference;
        this.img1 = img1;
        this.img2 = img2;
        this.category = category;
        this.idUsers = idUsers;
        this.timestamp = timestamp;
    }

    public String getId_post() {
        return id_post;
    }

    public void setId_post(String id_post) {
        this.id_post = id_post;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIdUsers() {
        return idUsers;
    }

    public void setIdUsers(String idUsers) {
        this.idUsers = idUsers;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}