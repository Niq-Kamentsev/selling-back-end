package com.main.sellplatform.controller.rest;


import com.main.sellplatform.controller.dto.UserDto;
import com.main.sellplatform.persistence.entity.User;
import com.main.sellplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(final UserService userService) {
        this.userService = userService;
    }
    @GetMapping(value = "/id", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserDto getUser(){
        User user = userService.getUser();
        return new UserDto(user.getFirstName(), user.getFirstName());

    }
    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }

}
