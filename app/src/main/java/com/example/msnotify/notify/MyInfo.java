package com.example.msnotify.notify;

public class MyInfo {

    private int start;
    private int till;
    private String state;
    private String quote;
    private String ver;

    public MyInfo(){
    }

    public MyInfo(int start, int till, String state, String quote, String ver) {
        this.start = start;
        this.till = till;
        this.state = state;
        this.quote = quote;
        this.ver = ver;
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
}
