package com.app.journal.controller;

import com.app.journal.entity.User;
import com.app.journal.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name="Admin APIs",description = "get and create Admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/get-all-users")
    @Operation(summary="get all users")
    public ResponseEntity<?> getAllUsers() {
        List<User> allUsers = userService.getAll();
        if (allUsers != null && !allUsers.isEmpty()) {
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/create-admin")
    @Operation(summary="create a new admin")
    public void createAdmin(@RequestBody User user)
    {
        userService.saveAdmin(user);
    }
}
