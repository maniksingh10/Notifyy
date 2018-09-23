package com.example.msnotify.notify;

public class Info {

    private String year;
    private String notice;
    private String branch;
    private String tName;
    private String date;


    public Info(){

    }


    public Info(String year, String notice, String branch, String tName,String date) {
        this.year = year;
        this.notice = notice;
        this.branch = branch;
        this.tName = tName;
        this.date =date;
    }

    public String getDate() {
        if (date==null){
            return "Hello";
        }else{
            return date;
        }

    }

    public String getYear() {
        return year;
    }

    public String getNotice() {
        return notice;
    }

    public String getBranch() {
        return branch;
    }

    public String gettName() {
        return tName;
    }
}
