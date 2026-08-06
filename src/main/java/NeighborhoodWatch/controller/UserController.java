package NeighborhoodWatch.controller;

import NeighborhoodWatch.entity.User;
import NeighborhoodWatch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }
    @PostMapping("/login")
public User login(@RequestBody User user) {
    return userService.loginUser(user.getEmail(), user.getPassword());
}
}
