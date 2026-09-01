package com.example.jtcp.userdetails;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserStore implements UserStore {

    private final Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public boolean exists(String username) {
        return users.containsKey(username);
    }

    @Override
    public boolean createIfAbsent(User user) {
        User existingUser = users.putIfAbsent(
                user.getUsername(),
                user
        );

        if (existingUser == null) {
            System.out.println("Saved user: " + user.getUsername());
            System.out.println("Users in store: " + users.keySet());
            return true;
        }

        return false;
    }

    @Override
    public User find(String username) {
        return users.get(username);
    }
}