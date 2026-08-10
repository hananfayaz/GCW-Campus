package com.example.gcwcampus.models;

public class FirebaseStudentModel {
    String name,address,phNumber,dob,email,regNo,gender,password;

    public FirebaseStudentModel() {
    }

    public FirebaseStudentModel(String name, String address, String phNumber, String dob, String email, String regNo, String gender, String password) {
        this.name = name;
        this.address = address;
        this.phNumber = phNumber;
        this.dob = dob;
        this.email = email;
        this.regNo = regNo;
        this.gender = gender;
        this.password = password;
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

    public String getPhNumber() {
        return phNumber;
    }

    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
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
}
