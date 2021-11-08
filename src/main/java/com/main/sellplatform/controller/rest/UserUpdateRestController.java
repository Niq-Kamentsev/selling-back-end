package com.main.sellplatform.controller.rest;

import com.main.sellplatform.controller.dto.userdto.UserUpdateEmailDTO;
import com.main.sellplatform.controller.dto.userdto.UserUpdatePasswordDTO;
import com.main.sellplatform.persistence.entity.User;
import com.main.sellplatform.service.UserUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:4040")
@RestController
@RequestMapping("api/update")
public class UserUpdateRestController {
    private final UserUpdateService userUpdateService;


    @Autowired
    public UserUpdateRestController(UserUpdateService userUpdateService) {
        this.userUpdateService = userUpdateService;
    }

    @PreAuthorize("hasAnyAuthority('user:update')")
    @PutMapping(value = "/updatePassword")
    public ResponseEntity<?> updateUserPassword(@RequestBody @Valid UserUpdatePasswordDTO requestDTO, HttpServletRequest request, HttpServletResponse response){
        User userByEmail = userUpdateService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        userUpdateService.updateUserPassword(userByEmail, requestDTO.getNewPassword(), requestDTO.getOldPassword());
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request,response, null);
        return ResponseEntity.ok("success");

    }


    @PreAuthorize("hasAnyAuthority('user:update')")
    @PutMapping(value = "/updateEmail")
    public ResponseEntity<?> updateUserEmail(@RequestBody @Valid UserUpdateEmailDTO requestDTO){
        User userByEmail = userUpdateService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        userUpdateService.updateUserEmail(userByEmail, requestDTO.getNewEmail());
        return ResponseEntity.ok("success");

    }



}
