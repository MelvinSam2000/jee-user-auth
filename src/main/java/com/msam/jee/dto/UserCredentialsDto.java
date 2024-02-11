package com.msam.jee.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserCredentialsDto {
    @JsonbProperty("username")
    String username;
    @JsonbProperty("raw_password")
    String rawPassword;
}
