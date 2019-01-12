package com.example.msnotify.notify;

public class UserInfo {

    private String name;
    private String course;
    private String mobile;
    private String token;
    private String device;
    private String year;
    private long timeinms;
    private int attendance;
    private String date_attendace;
    public UserInfo(){

    }
    public UserInfo(String name, String course, String mobile,String token,String device,String year,long timeinms,int attendance,String date_attendace) {
        this.name = name;
        this.course = course;
        this.mobile = mobile;
        this.token = token;
        this.device =device;
        this.year=year;
        this.timeinms = timeinms;
        this.attendance = attendance;
        this.date_attendace=date_attendace;
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

    public int getAttendance(){return attendance;}

    public String getDate_attendace(){return date_attendace;}
}
