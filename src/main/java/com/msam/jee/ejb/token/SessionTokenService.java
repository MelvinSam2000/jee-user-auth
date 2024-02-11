package com.msam.jee.ejb.token;

import com.msam.jee.ejb.user.RawPassUserRepo;
import com.msam.jee.ejb.user.UserRepo;
import jakarta.ejb.*;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.msam.jee.ConfigurableConstants.FLUSH_EXPIRED_SESSIONS_PERIOD_SECS;


@Slf4j
@Singleton
public final class SessionTokenService implements TokenService {

    private final Map<String, SessionToken> sessions = new HashMap<>();

    @Inject
    @ConfigProperty(name = "auth.token.expiration-time-secs")
    private int expirationTimeSecs;

    @EJB
    private UserRepo userRepo;

    @Override
    @Lock(LockType.READ)
    public boolean validate(String username, SessionToken token) {
        if (!sessions.containsKey(username)) {
            return false;
        }
        if (token.expired()) {
            log.info("User {} tried validating an expired token: {}", username, token.getRawToken());
            return false;
        }
        return sessions.get(username).getRawToken().equals(token.getRawToken());
    }

    @Override
    @Lock(LockType.WRITE)
    public Optional<SessionToken> login(String username, String password) {
        if (!userRepo.validateCredentials(username, password)) {
            log.info("Invalid credentials from user [{}]", username);
            return Optional.empty();
        }
        if (sessions.containsKey(username)) {
            log.info("User [{}] already logged in", username);
            return Optional.empty();
        }
        SessionToken token = new SessionToken(expirationTimeSecs);
        sessions.put(username, token);
        return Optional.of(token);
    }

    @Override
    @Lock(LockType.WRITE)
    public void logout(String username, SessionToken token) {
        log.info("User [{}] has logged out", username);
        sessions.remove(username);
    }

    @Schedule(second = FLUSH_EXPIRED_SESSIONS_PERIOD_SECS)
    @Lock(LockType.WRITE)
    private void flushExpiredTokens() {
        log.trace("Flushing expired tokens...");
        sessions.entrySet().removeIf(entry ->
            entry.getValue().expired()
        );

    }
}
