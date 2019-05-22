package com.example.ra.finalproject;

public class Exam extends Item {
    private String title, subject;
    private long date;
    private int weight;

    public Exam(){}

    public Exam(String title, String subject, long date, int weight) {
        this.title = title;
        this.subject = subject;
        this.date = date;
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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
