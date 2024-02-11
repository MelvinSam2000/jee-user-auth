package com.msam.jee.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class TokenDto {
    @JsonbProperty("token")
    String token;
    @JsonbProperty("expiration_time")
    ZonedDateTime expirationTime;
}
