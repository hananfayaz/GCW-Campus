package com.example.gcwcampus.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "timetable")
public class TimetableEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "sem")
    private String sem;

    @ColumnInfo(name = "subject")
    private String subject;

    @ColumnInfo(name = "start_time")
    private String startTime;

    @ColumnInfo(name = "end_time")
    private String endTime;

    @ColumnInfo(name = "faculty")
    private String faculty;

    @ColumnInfo(name = "department")
    private String department;

    @ColumnInfo(name = "day")
    private String day;

    @ColumnInfo(name = "period")
    private String period;

    @Ignore
    public TimetableEntity(int id, String sem, String subject, String startTime, String endTime, String faculty, String department, String period, String day) {
        this.id = id;
        this.sem = sem;
        this.subject = subject;
        this.startTime = startTime;
        this.endTime = endTime;
        this.faculty = faculty;
        this.department = department;
        this.period = period;
        this.day = day;
    }

    public TimetableEntity(String sem, String subject, String startTime, String endTime, String faculty, String department, String period, String day) {
        this.sem = sem;
        this.subject = subject;
        this.startTime = startTime;
        this.endTime = endTime;
        this.faculty = faculty;
        this.department = department;
        this.period = period;
        this.day = day;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
