package com.app.journal.controller;

import com.app.journal.api.response.WeatherResponse;
import com.app.journal.entity.User;
import com.app.journal.repository.UserRepository;
import com.app.journal.service.UserService;
import com.app.journal.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name="User APIs",description = "update and delete user and weather info")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeatherService weatherService;

//    @GetMapping("/get-all-users")
//    public List<User> getAllUsers() {
//        return userService.getAll();
//    }

    @PutMapping("/update-user")
    @Operation(summary="update username, password of a user")
    public ResponseEntity<?> updateUser(@RequestBody User user) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User storedUser = userService.findByUserName(userName);

        if (storedUser != null) {

            storedUser.setUserName(user.getUserName());

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                storedUser.setPassword(userService.encodePassword(user.getPassword()));
            }

            userService.saveUser(storedUser);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete")
    @Operation(summary="Delete a user")
    public ResponseEntity<?> deleteByUserName()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUserName(authentication.getName());
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
    }

    @GetMapping("/get-weather")
    @Operation(summary="get weather information of a city")
    public ResponseEntity<?> getweather()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse response = weatherService.getWeather("Mumbai");
        String message = "Current weather for "+response.getLocation().getName()+" is "+response.getCurrent().getTemperature();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }
}
