package com.example.ra.finalproject;

public class Subject {
    private String sid, name;

    public Subject() {}

    public Subject(String name) {
        this.name = name;
    }

    public String getSid() {return sid;}

    public void setSid(String sid) {this.sid = sid;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}
}
