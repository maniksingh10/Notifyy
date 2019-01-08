package com.example.msnotify.notify;

public class UserInfo {

    private String name;
    private String course;
    private String mobile;
    private String token;
    private String device;
    private String year;
    private long timeinms;
    public UserInfo(){

    }
    public UserInfo(String name, String course, String mobile,String token,String device,String year,long timeinms) {
        this.name = name;
        this.course = course;
        this.mobile = mobile;
        this.token = token;
        this.device =device;
        this.year=year;
        this.timeinms = timeinms;
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

    public long getTimeinms() {
        return timeinms;
    }
}
