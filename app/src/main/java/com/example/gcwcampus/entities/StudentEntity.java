package com.example.gcwcampus.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "student")
public class StudentEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "ph_no")
    private String phNo;

    @ColumnInfo(name = "dob")
    private String dob;

    @ColumnInfo(name = "sem")
    private String semester;

    @ColumnInfo(name = "course")
    private String course;

    @ColumnInfo(name = "roll_no")
    private String rollNo;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "reg_no")
    private String regNo;

    @ColumnInfo(name = "gender")
    private String gender;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "image_path")
    private String imgPath;

    @Ignore
    public StudentEntity(int id, String name, String address, String phNo, String dob, String semester, String course, String rollNo, String email, String regNo, String gender, String password) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phNo = phNo;
        this.dob = dob;
        this.semester = semester;
        this.course = course;
        this.rollNo = rollNo;
        this.email = email;
        this.regNo = regNo;
        this.gender = gender;
        this.password = password;
    }

    public StudentEntity(String name, String address, String phNo, String dob, String semester, String course, String rollNo, String email, String regNo, String gender, String password) {
        this.name = name;
        this.address = address;
        this.phNo = phNo;
        this.dob = dob;
        this.semester = semester;
        this.course = course;
        this.rollNo = rollNo;
        this.email = email;
        this.regNo = regNo;
        this.gender = gender;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhNo() {
        return phNo;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
