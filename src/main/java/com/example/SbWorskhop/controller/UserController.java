package com.example.SbWorskhop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.SbWorskhop.entity.User;
import com.example.SbWorskhop.entity.UserRole;
import com.example.SbWorskhop.service.UserService;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails); 
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    // for only role updates
    @PatchMapping("/{id}/role") // partial update of a resource
    public ResponseEntity<User> updateUserRole(@PathVariable Long id, @RequestBody RoleUpdateRequest roleRequest) {
        User updatedUser = userService.setUserRole(id, roleRequest.getRole());
        return ResponseEntity.ok(updatedUser);
    }

    // static inner wrapper class for role update request
    private static class RoleUpdateRequest {
        @JsonProperty("role") // enum cannot be sent as a request body, @JsonProperty must be used instead
        private UserRole role;

        public UserRole getRole() {
            return role;
        }
        public void setRole(UserRole role) {
            this.role = role;
        }
    }
}
