package com.msam.jee.ejb.token;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.msam.jee.ConfigurableConstants.SESSION_EXPIRATION_TIME_SECS;

@Data
@ToString
@EqualsAndHashCode
public class SessionToken {

    private String rawToken;
    private ZonedDateTime expirationTime;

    public SessionToken() {
        rawToken = UUID.randomUUID().toString();
        expirationTime = ZonedDateTime.now().plusSeconds(SESSION_EXPIRATION_TIME_SECS);
    }

    public boolean expired() {
        return ZonedDateTime.now().isAfter(expirationTime);
    }
}
