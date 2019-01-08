package com.example.msnotify.notify;

public class StuAttendance {
    private int addedAttebd;
    private String date;
    private String oneStuTotalAttend;
    public StuAttendance(){

    }


    public StuAttendance(int addedAttebd, String date,String oneStuTotalAttend) {
        this.addedAttebd = addedAttebd;
        this.date = date;
        this.oneStuTotalAttend=oneStuTotalAttend;
    }

    public int getAddedAttebd() {
        return addedAttebd;
    }

    public String getDate() {
        return date;
    }

    public String getOneStuTotalAttend() {
        return oneStuTotalAttend;

    }
}
