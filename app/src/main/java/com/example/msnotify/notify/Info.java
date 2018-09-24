package com.example.msnotify.notify;

public class Info {

    private String year;
    private String notice;
    private String branch;
    private String tName;
    private String date;
    private String url;

    public Info(){

    }


    public Info(String year, String notice, String branch, String tName,String date,String url) {
        this.year = year;
        this.notice = notice;
        this.branch = branch;
        this.tName = tName;
        this.date =date;
        this.url=url;
    }

    public String getDate() {
        if (date==null){
            return "Hello";
        }else{
            return date;
        }

    }

    public String getUrl() {
        if (url!=null){
            return url; }else{
            return "https://firebasestorage.googleapis.com/v0/b/notify-f6631.appspot.com/o/uploads%2F1537800888101.png?alt=media&token=80364583-3358-4fd6-aca2-97ae28f414ff";

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
