package com.msam.jee.api;

import com.msam.jee.dto.UserCredentialsDto;
import com.msam.jee.ejb.user.UserRepo;
import jakarta.ejb.EJB;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Path("/user")
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
}
