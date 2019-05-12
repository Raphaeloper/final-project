package com.example.ra.finalproject;

public class Grade {
    private String gid;
    private String title, subject;
    private long date;
    private int weight;
    private int num;

    public Grade() {}

    public Grade(String title, String subject, long date, int weight, int num) {
        this.title = title;
        this.subject = subject;
        this.date = date;
        this.weight = weight;
        this.num = num;
    }

    public String getGid() { return gid; }

    public void setGid(String gid) { this.gid = gid; }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
