package com.example.ra.finalproject;

import java.util.Date;

public class Exam {
    private String eid;
    private String title, subject;
    private String date;
    private double weight;

    public Exam(String title, String subject, String date, double weight) {
        this.title = title;
        this.subject = subject;
        this.date = date;
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

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
