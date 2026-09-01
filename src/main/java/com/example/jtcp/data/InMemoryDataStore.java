package com.example.jtcp.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDataStore implements DataStore {

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, String>> data =
            new ConcurrentHashMap<>();

    @Override
    public void set(String username, String key, String value) {
        data.computeIfAbsent(
                username,
                k -> new ConcurrentHashMap<>()
        ).put(key, value);
    }

    @Override
    public String get(String username, String key) {
        ConcurrentHashMap<String, String> userData = data.get(username);

        if (userData == null) {
            return null;
        }

        return userData.get(key);
    }

    @Override
    public boolean delete(String username, String key) {
        ConcurrentHashMap<String, String> userData = data.get(username);

        if (userData == null) {
            return false;
        }

        return userData.remove(key) != null;
    }

    @Override
    public List<String> getKeys(String username) {
        ConcurrentHashMap<String, String> userData = data.get(username);

        if (userData == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(userData.keySet());
    }
}