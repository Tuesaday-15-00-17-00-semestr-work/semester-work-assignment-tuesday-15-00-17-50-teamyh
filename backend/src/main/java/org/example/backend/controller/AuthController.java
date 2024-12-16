package org.example.backend.controller;

import org.example.backend.model.User;
import org.example.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // Установка роли USER по умолчанию
        user.setRole("USER");
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        // Проверка входных данных
        if (user.getUsername() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Username or password cannot be null");
        }

        // Проверка пользователя
        Optional<User> validatedUser = userService.validateUser(user.getUsername(), user.getPassword());
        if (validatedUser.isPresent()) {
            User loggedInUser = validatedUser.get();
            return ResponseEntity.ok(loggedInUser.getRole()); // Вернуть роль пользователя
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}
