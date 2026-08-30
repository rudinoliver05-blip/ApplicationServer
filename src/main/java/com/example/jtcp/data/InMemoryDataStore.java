package com.example.jtcp.data;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
public class InMemoryDataStore implements DataStore{
    private final Map<String, Map<String, String>> data = new HashMap<>();

    public void set(String username, String key, String value) {
        data.computeIfAbsent(username, k -> new HashMap<>())
                .put(key, value);
    }

    public String get(String username, String key) {
        Map<String, String> userData = data.get(username);
        if (userData == null) {
            return null;
        }
        return userData.get(key);
    }
    public boolean delete(String username, String key) {
        Map<String, String> userData = data.get(username);

        if (userData == null) {
            return false;
        }

        return userData.remove(key) != null;
    }
    public List<String> getKeys(String username) {
        Map<String, String> userData = data.get(username);

        if (userData == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(userData.keySet());
    }
}
