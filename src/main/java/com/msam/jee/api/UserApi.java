package com.msam.jee.api;

import com.msam.jee.dto.UserCredentialsDto;
import com.msam.jee.ejb.user.UserRepo;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserApi {

    @EJB
    UserRepo userRepo;

    @POST
    public void createUser(UserCredentialsDto userCredentials) {
        log.trace("Calling UserApi.createUser w {}", userCredentials);
        String username = userCredentials.getUsername();
        String rawPassword = userCredentials.getRawPassword();
        userRepo.createUser(username, rawPassword);
    }

    @DELETE
    public void deleteUser(String username) {
        log.trace("Calling UserApi.deleteUser w {}", username);
        userRepo.deleteUser(username);
    }

    @GET
    public List<String> getUsers() {
        log.trace("Calling UserApi.getUsers");
        return userRepo.getUsernames();
    }

    @GET
    @Path("/ping")
    public String ping() {
        log.info("Received PING");
        return "UserApi replied: pong";
    }
}
