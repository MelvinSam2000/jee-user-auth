package com.msam.jee.dto;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class TokenDto {
    String token;
    Optional<ZonedDateTime> expirationTime;
}
