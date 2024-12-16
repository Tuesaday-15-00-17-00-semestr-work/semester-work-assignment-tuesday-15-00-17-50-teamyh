package org.example.backend.service;

import org.example.backend.model.User;
import org.example.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Validate user by username and password.
     */
    public Optional<User> validateUser(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(rawPassword, user.getPassword()));
    }

    /**
     * Get all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get a user by ID.
     */
    public Optional<User> getUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return userRepository.findById(id);
    }

    /**
     * Get a user by username.
     */
    public Optional<User> getUserByUsername(String username) {
        validateNonEmpty(username, "Username");
        return userRepository.findByUsername(username);
    }

    /**
     * Get a user by email.
     */
    public Optional<User> getUserByEmail(String email) {
        validateNonEmpty(email, "Email");
        return userRepository.findByEmail(email);
    }

    /**
     * Register a new user.
     */
    public User registerUser(User user) {
        validateUserDetails(user);

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already taken");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Delete a user by ID.
     */
    public void deleteUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        userRepository.deleteById(userId);
    }

    /**
     * Update user details.
     */
    public User updateUser(Long userId, User userDetails) {
        if (userId == null || userDetails == null) {
            throw new IllegalArgumentException("User ID or user details cannot be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found"));

        if (userDetails.getUsername() != null) {
            user.setUsername(userDetails.getUsername());
        }

        if (userDetails.getEmail() != null) {
            if (userRepository.existsByEmail(userDetails.getEmail()) &&
                    !user.getId().equals(userDetails.getId())) {
                throw new IllegalArgumentException("Email is already in use");
            }
            user.setEmail(userDetails.getEmail());
        }

        if (userDetails.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        return userRepository.save(user);
    }

    /**
     * Validate non-empty fields.
     */
    private void validateNonEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
    }

    /**
     * Validate user details.
     */
    private void validateUserDetails(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        validateNonEmpty(user.getUsername(), "Username");
        validateNonEmpty(user.getEmail(), "Email");
        validateNonEmpty(user.getPassword(), "Password");
    }
}
