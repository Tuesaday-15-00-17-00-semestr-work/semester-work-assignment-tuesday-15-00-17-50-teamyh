package org.example.frontend;

import org.example.backend.model.User;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AuthenticationService {

    private static final String BASE_URL = "http://localhost:8080/api/auth";
    private static final int TIMEOUT = 5000;

    /**
     * Вход пользователя и возвращение объекта User с ролью
     */
    public static User login(String username, String password) {
        String endpoint = "/login";
        String requestBody = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
        String response = sendPostRequest(endpoint, requestBody);

        if (response != null) {
            if (response.contains("Invalid username or password")) {
                System.err.println("Login failed: Invalid username or password");
                return null;
            } else if (response.contains("USER") || response.contains("ADMIN")) {
                System.out.println("Login successful!");

                // Создание объекта User с ролью
                User user = new User();
                user.setUsername(username);
                user.setRole(response.trim()); // Роль из ответа
                return user;
            }
        }

        System.err.println("Login failed: Unknown response");
        return null;
    }

    /**
     * Регистрация нового пользователя
     */
    public static boolean register(String username, String password, String email) {
        String endpoint = "/register";
        String requestBody = String.format("{\"username\":\"%s\",\"password\":\"%s\",\"email\":\"%s\"}", username, password, email);
        String response = sendPostRequest(endpoint, requestBody);

        if (response != null && response.contains("User registered successfully")) {
            System.out.println("Registration successful: " + response);
            return true;
        } else {
            System.err.println("Registration failed: " + response);
            return false;
        }
    }

    /**
     * Метод для отправки POST-запроса
     */
    private static String sendPostRequest(String endpoint, String jsonBody) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(BASE_URL + endpoint);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            InputStream inputStream = (responseCode >= 200 && responseCode < 300)
                    ? connection.getInputStream()
                    : connection.getErrorStream();

            if (inputStream != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line.trim());
                    }
                    return response.toString();
                }
            }
        } catch (Exception e) {
            System.err.println("Error during POST request: " + e.getMessage());
        } finally {
            if (connection != null) connection.disconnect();
        }
        return null;
    }
}
