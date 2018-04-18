package com.emergency.signal.entity;

import android.widget.EditText;

/**
 * Created by TONMOYPC on 4/8/2018.
 */

public class users {
    String uid;
    String created;
    String name;
    String time;
    String email;
    String photoUrl;
    String address;
    String gender;
    String deviceid;
    String phoneNumber;
    String role;
    String password;

    users(){

    }

    public users(String address, String created, String deviceid, String email, String gender, String name, String phoneNumber, String photoUrl, String role, String uid) {
        this.address = address;
        this.created = created;
        this.email = email;
        this.deviceid = deviceid;
        this.gender = gender;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.photoUrl = photoUrl;
        this.role = role;
        this.uid = uid;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {

        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
