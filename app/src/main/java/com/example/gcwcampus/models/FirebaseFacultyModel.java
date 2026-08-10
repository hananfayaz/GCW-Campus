package com.example.gcwcampus.models;

public class FirebaseFacultyModel {
    String name,address,dob,email,gender,password;

    public FirebaseFacultyModel() {
    }

    public FirebaseFacultyModel(String name, String address, String dob, String email, String gender, String password) {
        this.name = name;
        this.address = address;
        this.dob = dob;
        this.email = email;
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
}
