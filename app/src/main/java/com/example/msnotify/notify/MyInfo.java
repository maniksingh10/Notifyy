package com.example.msnotify.notify;

public class MyInfo {

    private int start;
    private int till;
    private String state;
    private String quote;
    private String ver;
    private String timetable1;
    private String timetable2;

    public MyInfo() {
    }

    public MyInfo(int start, int till, String state, String quote, String ver, String timetable1, String timetable2) {
        this.start = start;
        this.till = till;
        this.state = state;
        this.quote = quote;
        this.ver = ver;
        this.timetable1 = timetable1;
        this.timetable2 = timetable2;
    }


    public int getStart() {
        return start;
    }

    public String getVer() {
        return ver;
    }

    public int getTill() {
        return till;
    }

    public String getState() {
        return state;
    }

    public String getQuote() {
        return quote;
    }

    public String getTimetable1() {
        return timetable1;
    }

    public String getTimetable2() {
        return timetable2;
    }
}
