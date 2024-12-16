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
        validateNotNull(id, "ID");
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

        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encode password
        return userRepository.save(user);
    }

    /**
     * Delete a user by ID.
     */
    public void deleteUser(Long userId) {
        validateNotNull(userId, "User ID");
        userRepository.deleteById(userId);
    }

    /**
     * Update user details.
     */
    public User updateUser(Long userId, User userDetails) {
        validateNotNull(userId, "User ID");
        validateNotNull(userDetails, "User details");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found"));

        // Update username if provided
        if (userDetails.getUsername() != null && !userDetails.getUsername().isEmpty()) {
            user.setUsername(userDetails.getUsername());
        }

        // Update email with uniqueness check
        if (userDetails.getEmail() != null && !userDetails.getEmail().isEmpty()) {
            if (userRepository.existsByEmail(userDetails.getEmail()) &&
                    !user.getId().equals(userDetails.getId())) {
                throw new IllegalArgumentException("Email is already in use");
            }
            user.setEmail(userDetails.getEmail());
        }

        // Update password only if provided
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        return userRepository.save(user);
    }

    /**
     * Validate that a value is not null.
     */
    private void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
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
        validateNotNull(user, "User");
        validateNonEmpty(user.getUsername(), "Username");
        validateNonEmpty(user.getEmail(), "Email");
        validateNonEmpty(user.getPassword(), "Password");
    }
}
