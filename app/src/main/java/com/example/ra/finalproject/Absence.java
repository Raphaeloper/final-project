package com.example.ra.finalproject;

public class Absence {
    private String aid;
    private String subject;
    private long date;
    private boolean approved;

    public Absence() {}

    public Absence(String subject, long date, boolean approved) {
        this.subject = subject;
        this.date = date;
        this.approved = approved;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
