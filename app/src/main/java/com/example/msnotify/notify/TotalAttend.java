package com.example.msnotify.notify;

public class TotalAttend {
    private int totalAtttendd;
    private String totalUpDate;

    public TotalAttend(){

    }
    public TotalAttend(int totalAtttendd, String totalUpDate) {
        this.totalAtttendd = totalAtttendd;
        this.totalUpDate = totalUpDate;
    }

    public int getTotalAtttendd() {
        return totalAtttendd;
    }

    public String getTotalUpDate() {
        return totalUpDate;
    }
}
