package com.msam.jee.api;

import com.msam.jee.dto.TokenDto;
import com.msam.jee.dto.UserCredentialsDto;
import com.msam.jee.ejb.token.SessionToken;
import com.msam.jee.ejb.token.TokenService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
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

    @GET
    @Path("/ping")
    public String ping() {
        log.info("Received PING");
        return "AuthApi replied: pong";
    }
}
