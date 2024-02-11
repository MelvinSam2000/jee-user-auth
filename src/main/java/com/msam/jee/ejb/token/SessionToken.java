package com.msam.jee.ejb.token;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Data
@ToString
@EqualsAndHashCode
public class SessionToken {

    private String rawToken;
    private Optional<ZonedDateTime> expirationTime;

    public SessionToken(long expirationTimeSecs) {
        rawToken = UUID.randomUUID().toString();
        if (expirationTimeSecs == 0) {
            expirationTime = Optional.empty();
        } else {
            expirationTime = Optional.of(ZonedDateTime.now().plusSeconds(expirationTimeSecs));
        }
    }

    public boolean expired() {
        if (expirationTime.isEmpty()) {
            return false;
        }
        return ZonedDateTime.now().isAfter(expirationTime.get());
    }
}
