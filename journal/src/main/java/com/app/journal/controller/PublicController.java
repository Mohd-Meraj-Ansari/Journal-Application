package com.app.journal.controller;

import com.app.journal.entity.User;
import com.app.journal.service.UserDetailsServiceImpl;
import com.app.journal.service.UserService;
import com.app.journal.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("/public")
@Tag(name="Public APIs",description = "create and login user, health-check")
public class PublicController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/create-user")
    @Operation(summary="create a new user")
    public void createUser(@RequestBody User user) {
        userService.saveNewUser(user);
    }

    @PostMapping("/login")
    @Operation(summary="login for a user")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception while createAuthentication token ", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/health-check")
    @Operation(summary="health-check of Application")
    public String healthCheck() {
        return "ok";
    }
}
