package com.msam.jee.api;

import com.msam.jee.ejb.user.UserRepo;
import jakarta.ejb.EJB;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import java.util.List;

@Path("/user")
public class UserApi {

    @EJB
    UserRepo userRepo;

    @GET
    public String ping() {
        return "pong";
    }

    @POST
    public void createUser(String username, String rawPassword) {
        userRepo.createUser(username, rawPassword);
    }

    @DELETE
    public void deleteUser(String username) {
        userRepo.deleteUser(username);
    }

    @GET
    public List<String> getUsers() {
        return userRepo.getUsernames();
    }
}
