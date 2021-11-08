package com.main.sellplatform.controller.rest;

import com.main.sellplatform.controller.dto.userdto.UserDto;
import com.main.sellplatform.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;


@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("api/v1/registration")
public class RegistrationUserRestController {

    private final UserRegistrationService userRegistrationService;

    @Autowired
    public RegistrationUserRestController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @PostMapping(value = "/registrationUser")
    public ResponseEntity<?> registrationUser(@Valid @RequestBody UserDto user){
        if (userRegistrationService.registrationUser(user.getUser())){
            return ResponseEntity.ok("success");
        }
        else return new ResponseEntity<>("such user already exists ", HttpStatus.CONFLICT);
    }

    @PostMapping(value = "/activation{code}")
    public ResponseEntity<?> activateAccount(@PathVariable String code){
        if(Objects.isNull(code))
            return new ResponseEntity<>("code is empty", HttpStatus.NO_CONTENT);
        if(userRegistrationService.activeUser(code))
            return ResponseEntity.ok("success");
        else return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
    }

}
