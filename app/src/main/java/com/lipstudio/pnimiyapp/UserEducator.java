package com.lipstudio.pnimiyapp;

import java.util.ArrayList;

public class UserEducator extends User{


    public UserEducator(String firstName, String lastName, long id, String password, ArrayList<String> groups, String phoneNumber){
        super(firstName,lastName,id,password,groups,phoneNumber);
    }

    @Override
    public String toString() {
        return super.toString() + " UserEducator";
    }
}
