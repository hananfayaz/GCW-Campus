package com.example.gcwcampus.models;

public class RecyclerTimetableModel {
    String subject, startTime, endTime, faculty, period, day, department;

    public RecyclerTimetableModel(){

    }

    public RecyclerTimetableModel(String subject, String startTime, String endTime, String faculty, String period, String day, String department) {
        this.subject = subject;
        this.startTime = startTime;
        this.endTime = endTime;
        this.faculty = faculty;
        this.period = period;
        this.day = day;
        this.department = department;
    }

    public RecyclerTimetableModel(String subject, String startTime, String endTime, String faculty, String period, String day) {
        this.subject = subject;
        this.startTime = startTime;
        this.endTime = endTime;
        this.faculty = faculty;
        this.period = period;
        this.day = day;
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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
