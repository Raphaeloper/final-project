package com.example.ra.finalproject;

import java.util.Date;

public class Exam {
    private String eid;
    private String title, subject;
    private String date;
    private int weight;

    public Exam(){}

    public Exam(String title, String subject, String date, int weight) {
        this.title = title;
        this.subject = subject;
        this.date = date;
        this.weight = weight;
    }

    public String getEid() { return eid; }

    public void setEid(String eid) { this.eid = eid; }

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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
