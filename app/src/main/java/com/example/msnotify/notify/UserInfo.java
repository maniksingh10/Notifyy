package com.example.msnotify.notify;

public class UserInfo {

    private String name;
    private String course;
    private String mobile;

    public UserInfo(){

    }
    public UserInfo(String name, String course, String mobile) {
        this.name = name;
        this.course = course;
        this.mobile = mobile;
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
}
