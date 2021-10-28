package com.main.sellplatform.controller.rest;

import com.main.sellplatform.persistence.entity.User;
import com.main.sellplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("api")
public class UserRestController {


    private final UserService userService;

    @Autowired
    public UserRestController(final UserService userService) {
        this.userService = userService;
    }


    @PreAuthorize("hasAnyAuthority('user:read')")
    @GetMapping(value = "/getUsers", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<User> getUsersJdbc(){
        return userService.getUsers();

    }


    @PreAuthorize("hasAnyAuthority('user:write')")
    @PostMapping(value = "/postUser", produces = {MediaType.APPLICATION_JSON_VALUE})
    public void createUser(@RequestBody User user ) {
        System.out.println(user);
        userService.saveUser(user);
    }


    @PreAuthorize("hasAnyAuthority('user:read')")
    @GetMapping(value = "/getUser{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public User getUser(@PathVariable Integer id){
        return userService.getUser(id);
    }

    @PreAuthorize("hasAnyAuthority('user:delete')")
    @DeleteMapping(value = "/delete{id}", produces = {MediaType.APPLICATION_JSON_VALUE} )
    public void deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);
    }

}
