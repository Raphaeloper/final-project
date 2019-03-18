package com.example.ra.finalproject;

import java.util.Date;

public class Absence {
    private String aid;
    private String subject;
    private String date;
    private boolean approved;

    public Absence(String subject, String date, boolean approved) {
        this.subject = subject;
        this.date = date;
        this.approved = approved;
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

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
