package com.msam.jee.ejb.token;

import com.msam.jee.ejb.user.RawPassUserRepo;
import com.msam.jee.ejb.user.UserRepo;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.*;
import lombok.extern.slf4j.Slf4j;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Singleton
public class SessionTokenService implements TokenService, SessionTokenServiceMBean {

    final Map<String, SessionToken> sessions = new HashMap<>();

    ObjectName jmxObjName;

    @EJB
    UserRepo userRepo;

    @PostConstruct
    public void registerMBean() {
        try {
            String packageStr = SessionTokenService.class.getPackageName();
            String classStr = SessionTokenService.class.getSimpleName();
            jmxObjName = new ObjectName(String.format("%s:type=%s", packageStr, classStr));
            ManagementFactory.getPlatformMBeanServer().registerMBean(this, jmxObjName);
            log.info("MBean registered [{}]", jmxObjName.toString());
        } catch (MalformedObjectNameException |
                 InstanceAlreadyExistsException |
                 MBeanRegistrationException |
                 NotCompliantMBeanException e) {
            log.error("Could not register MBean for SessionTokenService", e);
        }
    }

    @PreDestroy
    public void unregisterMBean() {
        try {
            ManagementFactory.getPlatformMBeanServer().unregisterMBean(jmxObjName);
        } catch (InstanceNotFoundException | MBeanRegistrationException e) {
            log.error("Could not unregister MBean for RawPassUserRepo", e);
        }
    }

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
    public Optional<SessionToken> login(String username, String rawPassword) {
        if (!userRepo.validateCredentials(username, rawPassword)) {
            log.info("Invalid credentials from user [{}]", username);
            return Optional.empty();
        }
        if (sessions.containsKey(username)) {
            log.info("User [{}] already logged in", username);
            return Optional.empty();
        }
        SessionToken token = new SessionToken();
        sessions.put(username, token);
        return Optional.of(token);
    }

    @Override
    @Lock(LockType.WRITE)
    public void logout(String username, SessionToken token) {
        log.info("User [{}] has logged out", username);
        sessions.remove(username);
    }

    //@Schedule(second = FLUSH_EXPIRED_SESSIONS_PERIOD_SECS)
    @Schedule(hour = "*", minute = "*", second = "*/30", persistent = false)
    @Lock(LockType.WRITE)
    private void flushExpiredTokens() {
        log.trace("Flushing expired tokens...");
        sessions.entrySet().removeIf(entry ->
            entry.getValue().expired()
        );
    }

    @Override
    public String getSessions() {
        return sessions.toString();
    }
}
