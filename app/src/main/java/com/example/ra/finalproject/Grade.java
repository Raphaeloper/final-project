package com.example.ra.finalproject;

import java.util.Date;

public class Grade {
    private String gid;
    private String title, subject;
    private String date;
    private double weight;
    private int num;

    public Grade(String title, String subject, String date, double weight, int num) {
        this.title = title;
        this.subject = subject;
        this.date = date;
        this.weight = weight;
        this.num = num;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
