package com.example.jtcp.storage;

public interface UserStore {
    boolean exists(String username);

    void save(User user);

    User find(String username);
}
