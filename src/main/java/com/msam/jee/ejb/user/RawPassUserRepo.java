package com.msam.jee.ejb.user;

import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class RawPassUserRepo implements UserRepo {

    Map<String, String> usersMap = new HashMap<>();

    @Override
    @Lock(LockType.READ)
    public boolean validateCredentials(String username, String rawPassword) {

        if (!usersMap.containsKey(username)) {
            return false;
        }

        return usersMap.get(username).equals(rawPassword);
    }

    @Override
    @Lock(LockType.WRITE)
    public void createUser(String username, String rawPassword) {
        if (usersMap.containsKey(username)) {
            return;
        }
        usersMap.put(username, rawPassword);
    }

    @Override
    @Lock(LockType.WRITE)
    public boolean deleteUser(String username) {
        if (!usersMap.containsKey(username)) {
            return false;
        }
        usersMap.remove(username);
        return true;
    }

    @Override
    @Lock(LockType.READ)
    public List<String> getUsernames() {
        return usersMap.keySet().stream().toList();
    }
}
