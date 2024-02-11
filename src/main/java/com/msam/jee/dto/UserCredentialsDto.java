package com.msam.jee.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserCredentialsDto {
    String username;
    String rawPassword;
}
