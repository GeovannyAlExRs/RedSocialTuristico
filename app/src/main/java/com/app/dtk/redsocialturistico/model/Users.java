package com.app.dtk.redsocialturistico.model;

public class Users {

    private String id_users;
    private String username;
    private String phone;
    private String email;
    private String password;
    private long timestamp;
    private String img_perfil;
    private String img_cover;

    public Users() {
    }

    public Users(String id_users, String username, String phone, String email, String password, long timestamp, String img_perfil, String img_cover) {
        this.id_users = id_users;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.timestamp = timestamp;
        this.img_perfil = img_perfil;
        this.img_cover = img_cover;
    }

    public String getId_users() {
        return id_users;
    }

    public void setId_users(String id_users) {
        this.id_users = id_users;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImg_perfil() {
        return img_perfil;
    }

    public void setImg_perfil(String img_perfil) {
        this.img_perfil = img_perfil;
    }

    public String getImg_cover() {
        return img_cover;
    }

    public void setImg_cover(String img_cover) {
        this.img_cover = img_cover;
    }
}