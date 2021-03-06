package com.lipstudio.pnimiyapp;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private String firstName;
    private String lastName;
    private long id;
    private String password;
    private ArrayList<String> group;
    private String phoneNumber;

    public User(String firstName, String lastName, long id, String password, ArrayList<String> group, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.password = password;
        this.group = group;
        this.phoneNumber = phoneNumber;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getGroup() {
        return group;
    }

    public void setGroup(ArrayList<String> group) {
        this.group = group;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", id=" + id +
                ", password='" + password + '\'' +
                ", group=" + group +
                ", phoneNumber=" + phoneNumber +
                '}';
    }
}
