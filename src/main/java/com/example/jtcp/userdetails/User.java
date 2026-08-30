package com.example.jtcp.userdetails;

public class User {
    private String username;
    private String passowrd;
    public User(String usename,String password){
        this.username=usename;
        this.passowrd=password;
    }
    public String  getUsername(){
        return this.username;
    }
    public String  getPassword(){
        return this.passowrd;
    }

}
