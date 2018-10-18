package com.example.msnotify.notify;

public class UserInfo {

    private String name;
    private String course;
    private String mobile;
    private String token;
    public UserInfo(){

    }
    public UserInfo(String name, String course, String mobile,String token) {
        this.name = name;
        this.course = course;
        this.mobile = mobile;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public String getMobile() {
        return mobile;
    }

    public String getToken() {
        return token;
    }
}
