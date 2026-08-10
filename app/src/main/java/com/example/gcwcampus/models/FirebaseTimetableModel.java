package com.example.gcwcampus.models;

public class FirebaseTimetableModel {
    String subject,startTime,endTime,faculty,day;

    public FirebaseTimetableModel() {
    }

    public FirebaseTimetableModel(String subject, String startTime, String endTime, String faculty) {
        this.subject = subject;
        this.startTime = startTime;
        this.endTime = endTime;
        this.faculty = faculty;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
