package com.example.msnotify.notify;

public class UserInfo {

    private String name;
    private String course;
    private String mobile;
    private String token;
    private String device;
    private String year;
    public UserInfo(){

    }
    public UserInfo(String name, String course, String mobile,String token,String device,String year) {
        this.name = name;
        this.course = course;
        this.mobile = mobile;
        this.token = token;
        this.device =device;
        this.year=year;
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

    public String getDevice() {
        return device;
    }

    public String getYear() {
        return year;
    }
}
