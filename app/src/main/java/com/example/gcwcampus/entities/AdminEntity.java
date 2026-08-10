package com.example.gcwcampus.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "admin")
public class AdminEntity {
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

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "gender")
    private String gender;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "image_path")
    private String imgPath;

    public AdminEntity() {}

    @Ignore
    public AdminEntity(int id, String name, String address, String phNo, String dob, String email, String gender, String password){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phNo = phNo;
        this.dob = dob;
        this.email = email;
        this.gender = gender;
        this.password = password;
    }

    public AdminEntity(String name, String address, String phNo, String dob, String email, String gender, String password){
        this.name = name;
        this.address = address;
        this.phNo = phNo;
        this.dob = dob;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
