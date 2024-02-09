package com.msam.jee.ejb.user;

import jakarta.ejb.Singleton;

import java.util.List;

@Singleton
public class UserRepoEjb implements UserRepo {

    @Override
    public boolean validateCredentials(String username, String rawPassword) {
        return username.equals(rawPassword);
    }

    @Override
    public void createUser(String username, String rawPassword) {

    }

    @Override
    public boolean deleteUser(String username) {
        return false;
    }

    @Override
    public List<String> getUsernames() {
        return null;
    }
}
