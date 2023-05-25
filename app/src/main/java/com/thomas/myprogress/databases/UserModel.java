package com.thomas.myprogress.databases;

import java.time.*;

public class UserModel {
    private int id = 0;
    private String username;
    private String password;
    //private String regDate;

    public UserModel(String username, String password){
        this.id++;
        this.username = username;
        this.password = password;
        //this.regDate = regDate;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
