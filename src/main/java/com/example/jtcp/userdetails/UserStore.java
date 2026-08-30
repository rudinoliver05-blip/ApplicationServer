package com.example.jtcp.userdetails;

public interface UserStore {
    boolean exists(String username);

    void save(User user);

    User find(String username);
}
