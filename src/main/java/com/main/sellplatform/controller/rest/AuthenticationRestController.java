package com.main.sellplatform.controller.rest;


import com.main.sellplatform.controller.dto.AuthenticationRequestDTO;
import com.main.sellplatform.controller.dto.TokenRefreshRequest;
import com.main.sellplatform.exception.userexception.UserNotFoundByEmailException;
import com.main.sellplatform.persistence.entity.RefreshToken;
import com.main.sellplatform.persistence.entity.User;
import com.main.sellplatform.security.JwtTokenProvider;

import com.main.sellplatform.service.RefreshTokenService;
import com.main.sellplatform.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;




@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationRestController(AuthenticationManager authenticationManager, UserService userDao, JwtTokenProvider jwtTokenProvider, RefreshTokenService refreshTokenService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userService = userDao;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDTO request){
            User user = userService.getUserByEmail(request.getEmail());
            if(user == null){
                throw new UserNotFoundByEmailException("Invalid email or password",request.getEmail());
            }

            if(passwordEncoder.matches(request.getPassword(),user.getPassword())){
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

                String token = jwtTokenProvider.createToken(request.getEmail(), user.getRole().name());
                Map<Object,Object> response = new HashMap<>();
                response.put("email", request.getEmail());
                response.put("token", token);
                response.put("firstName",user.getFirstName());
                response.put("lastName",user.getLastName());
                response.put("phoneNumber",user.getPhoneNumber());

                response.put("refreshToken", refreshToken.getToken());
                return ResponseEntity.ok(response);
            }
        return new ResponseEntity<>("Invalid email or password", HttpStatus.BAD_REQUEST);
    }



    @CrossOrigin(origins = "http://localhost:4200/profile")
    @PostMapping(value = "/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response){
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request,response, null);

    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest refreshRequest){
        String refreshToken = refreshRequest.getRefreshToken();
        RefreshToken byToken = refreshTokenService.findByToken(refreshToken);
        if(refreshTokenService.verifyExpiration(byToken)){
            User user = byToken.getUser();
            String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole().name());
            Map<Object,Object> response = new HashMap<>();
            response.put("email", user.getEmail());
            response.put("token", token);
            response.put("refreshToken", refreshToken);
            return ResponseEntity.ok(response);
        }
        return new ResponseEntity<>("Refresh token invalid", HttpStatus.NOT_FOUND);

    }
}
