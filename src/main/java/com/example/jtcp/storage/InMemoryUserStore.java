package com.example.jtcp.storage;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class InMemoryUserStore implements UserStore {
    private final Map<String,User> users=new HashMap<>();
    @Override
    public boolean exists(String username){
        if(users.containsKey(username)){
            return Boolean.TRUE;
        }
        else{
          return   Boolean.FALSE;
        }
    }
    @Override
    public void save(User user){
        users.put(user.getUsername(),user);
        System.out.println("Saved user: " + user.getUsername());
        System.out.println("Users in store: " + users.keySet());
    }
    @Override
    public User find(String username) {
        return users.get(username);
    }

}
