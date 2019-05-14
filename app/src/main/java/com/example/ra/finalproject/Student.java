package com.example.ra.finalproject;

import java.util.ArrayList;

public class Student {
    private String uid, name, classroom, school;

    public Student() {
    }

    public Student(String uid, String name, String classroom, String school) {
        this.uid = uid;
        this.name = name;
        this.classroom = classroom;
        this.school = school;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

}
