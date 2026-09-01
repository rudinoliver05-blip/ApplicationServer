package com.example.jtcp.userdetails;

public interface UserStore {

    boolean exists(String username);

    boolean createIfAbsent(User user);

    User find(String username);
}