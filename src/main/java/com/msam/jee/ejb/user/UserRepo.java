package com.msam.jee.ejb.user;

import java.util.List;

public interface UserRepo {

    boolean validateCredentials(String username, String rawPassword);

    void createUser(String username, String rawPassword);
    boolean deleteUser(String username);

    List<String> getUsernames();
}
