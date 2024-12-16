package org.example.frontend;

import org.example.backend.model.User;
import org.example.backend.service.BookService;
import org.example.backend.service.UserService;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class AdminDashboard {

    private final UserService userService;
    private final BookService bookService;
    private final User currentUser;

    private Stage stage;

    public AdminDashboard(User currentUser, UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
        this.currentUser = currentUser;

        // Проверка роли
        if (!"ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            throw new IllegalStateException("Access denied: Only admins can access this dashboard.");
        }

        // Инициализация UI
        initializeUI();
    }

    private void initializeUI() {
        // Создаем окно для администратора
        stage = new Stage();
        stage.setTitle("Admin Dashboard - " + currentUser.getUsername());

        // Корневой контейнер
        VBox root = new VBox(10);

        // Отображение списка пользователей
        ListView<String> userListView = new ListView<>();
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            userListView.getItems().add("Username: " + user.getUsername() + ", Role: " + user.getRole());
        }

        // Кнопка для регистрации нового пользователя
        Button registerUserButton = new Button("Register New User");
        registerUserButton.setOnAction(e -> showUserRegistrationForm());

        // Кнопка управления книгами
        Button manageBooksButton = new Button("Manage Books");
        manageBooksButton.setOnAction(e -> showBookManagement());

        // Добавляем элементы управления на корневой контейнер
        root.getChildren().addAll(userListView, registerUserButton, manageBooksButton);

        // Устанавливаем сцену и показываем окно
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void showUserRegistrationForm() {
        // Заглушка для регистрации нового пользователя
        System.out.println("Opening user registration form...");
    }

    private void showBookManagement() {
        // Заглушка для управления книгами
        System.out.println("Opening book management interface...");
    }
}
