package com.furniturehome.controller;

import com.furniturehome.model.User;
import com.furniturehome.service.UserService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @PutMapping("/{id}")
    public User updateUser(@PathVariable UUID id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

}