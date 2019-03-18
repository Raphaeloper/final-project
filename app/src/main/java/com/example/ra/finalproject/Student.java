package com.example.ra.finalproject;

import java.util.ArrayList;

public class Student {// <rant> I hate that I can't use the word 'class', and 'grade' fits the grades better than 'mark' </rant>
    private String uid, name, classroom, school;
    private ArrayList<Grade> grades;
    private ArrayList<Absence> absences;
    private ArrayList<Exam> exams;

    public Student() {
    }

    public Student(String uid, String name, String classroom, String school) {
        this.uid = uid;
        this.name = name;
        this.classroom = classroom;
        this.school = school;
    }

    public Student(String uid, String name, String classroom, String school, ArrayList<Grade> grades, ArrayList<Absence> absences, ArrayList<Exam> exams) {
        this.uid = uid;
        this.name = name;
        this.classroom = classroom;
        this.school = school;
        this.grades = grades;
        this.absences = absences;
        this.exams = exams;
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

    public ArrayList<Grade> getGrades() {
        return grades;
    }

    public void setGrades(ArrayList<Grade> grades) {
        this.grades = grades;
    }

    public ArrayList<Absence> getAbsences() {
        return absences;
    }

    public void setAbsences(ArrayList<Absence> absences) {
        this.absences = absences;
    }

    public ArrayList<Exam> getExams() {
        return exams;
    }

    public void setExams(ArrayList<Exam> exams) {
        this.exams = exams;
    }
}
