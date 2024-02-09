package com.msam.jee.ejb.token;

import com.msam.jee.ejb.user.UserRepoEjb;
import jakarta.ejb.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Singleton
@Slf4j
public final class SessionTokenService implements TokenService {

    private final Map<String, String> sessions = new HashMap<>();

    @EJB
    private UserRepoEjb userRepo;

    @Override
    @Lock(LockType.READ)
    public boolean validate(String username, String token) {
        if (!sessions.containsKey(username)) {
            return false;
        }
        return sessions.get(username).equals(token);
    }

    @Override
    @Lock(LockType.WRITE)
    public Optional<String> login(String username, String password) {
        if (!userRepo.validateCredentials(username, password)) {
            log.info("Invalid credentials from user [{}]", username);
            return Optional.empty();
        }
        if (sessions.containsKey(username)) {
            return Optional.empty();
        }
        String token = UUID.randomUUID().toString();
        sessions.put(username, token);
        return Optional.of(token);
    }

    @Override
    @Lock(LockType.WRITE)
    public void logout(String username, String token) {
        sessions.remove(username);
    }
}
