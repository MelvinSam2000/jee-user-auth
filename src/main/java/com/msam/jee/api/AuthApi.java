package com.msam.jee.api;

import com.msam.jee.dto.TokenDto;
import com.msam.jee.dto.UserCredentialsDto;
import com.msam.jee.ejb.token.SessionToken;
import com.msam.jee.ejb.token.TokenService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@Path("/auth")
public class AuthApi {

    @EJB
    TokenService tokenService;

    @POST
    @Path("/login")
    public Optional<TokenDto> login(UserCredentialsDto userCredentials) {
        log.trace("Calling AuthApi.login w {}", userCredentials);

        String username = userCredentials.getUsername();
        String rawPassword = userCredentials.getRawPassword();

        Optional<SessionToken> tokenOptional = tokenService.login(username, rawPassword);

        if (tokenOptional.isEmpty()) {
            return Optional.empty();
        }

        SessionToken token = tokenOptional.get();

        return Optional.of(new TokenDto(
                token.getRawToken(),
                token.getExpirationTime()
        ));
    }

    @PUT
    @Path("/logout")
    public void logout() {
        log.trace("Calling AuthApi.logout");
        //tokenService.logout();
    }
}
