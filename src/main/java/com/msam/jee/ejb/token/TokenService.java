package com.msam.jee.ejb.token;

import jakarta.ejb.Local;

import java.util.Optional;

@Local
public interface TokenService {
    boolean validate(String username, SessionToken token);
    Optional<SessionToken> login(String username, String password);
    void logout(String username, SessionToken token);
}
