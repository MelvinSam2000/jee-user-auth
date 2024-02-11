package com.msam.jee.ejb.token;

import java.util.Optional;

public interface TokenService {
    boolean validate(String username, SessionToken token);
    Optional<SessionToken> login(String username, String password);
    void logout(String username, SessionToken token);
}
