package com.msam.jee.ejb.token;

import java.util.Optional;

public interface TokenService {
    boolean validate(String username, String token);
    Optional<String> login(String username, String password);
    void logout(String username, String token);
}
