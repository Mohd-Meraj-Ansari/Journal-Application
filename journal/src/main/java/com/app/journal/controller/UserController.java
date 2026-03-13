package com.app.journal.controller;

import com.app.journal.entity.User;
import com.app.journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create-user")
    public void createUser(@RequestBody User user) {
        userService.saveEntry(user);
    }

    @GetMapping("/get-all-users")
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @PutMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        User storedUser = userService.findByUserName(user.getUserName());
        if (storedUser != null) {
            storedUser.setUserName(user.getUserName());
            storedUser.setPassword(user.getPassword());
            userService.saveEntry(storedUser);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
