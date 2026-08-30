package com.example.jtcp.data;

import java.util.List;

public interface DataStore {
    public void set(String username, String key, String value);
    public String get(String username, String key);
    public boolean delete(String username, String key);
    public List<String> getKeys(String username);
}
