package org.example.frontend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.model.User;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DashboardPage {

    private final User currentUser;
    private static final String BOOKS_FILE = "books.json"; // Имя файла для хранения данных
    private final List<Book> books; // Локальный список книг
    private final ObjectMapper objectMapper;

    public DashboardPage(User user) {
        this.currentUser = user;
        this.objectMapper = new ObjectMapper();
        this.books = loadBooks();
        initializeUI();
    }

    /**
     * Загрузка книг из файла.
     */
    private List<Book> loadBooks() {
        try {
            File file = new File(BOOKS_FILE);
            if (file.exists()) {
                return objectMapper.readValue(file, new TypeReference<List<Book>>() {});
            }
        } catch (Exception e) {
            System.err.println("Error loading books: " + e.getMessage());
        }
        return new ArrayList<>(); // Возвращаем пустой список, если файла нет
    }

    /**
     * Сохранение книг в файл.
     */
    private void saveBooks() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(BOOKS_FILE), books);
        } catch (Exception e) {
            System.err.println("Error saving books: " + e.getMessage());
        }
    }

    private void initializeUI() {
        // Создание нового окна Dashboard
        Stage dashboardStage = new Stage();
        dashboardStage.setTitle("Dashboard - " + currentUser.getUsername());

        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label welcomeLabel = new Label("Welcome, " + currentUser.getUsername());
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Кнопка добавления книги
        Button addBookButton = new Button("Add Book");
        addBookButton.setOnAction(e -> showAddBookForm());

        // Кнопка просмотра всех книг
        Button viewBooksButton = new Button("View Books");
        viewBooksButton.setOnAction(e -> showBookList());

        // Кнопка "Мои книги"
        Button myBooksButton = new Button("My Books");
        myBooksButton.setOnAction(e -> showMyBooks());

        if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            Button registerUserButton = new Button("Register New User");
            registerUserButton.setOnAction(e -> showRegisterUserForm());
            root.getChildren().addAll(welcomeLabel, addBookButton, viewBooksButton, myBooksButton, registerUserButton);
        } else {
            root.getChildren().addAll(welcomeLabel, addBookButton, viewBooksButton, myBooksButton);
        }

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            saveBooks();
            dashboardStage.close();
            System.out.println("Logged out successfully!");
        });
        root.getChildren().add(logoutButton);

        Scene scene = new Scene(root, 350, 400);
        dashboardStage.setScene(scene);
        dashboardStage.show();
    }

    /**
     * Метод отображения формы добавления книги.
     */
    private void showAddBookForm() {
        Stage addBookStage = new Stage();
        addBookStage.initModality(Modality.APPLICATION_MODAL);
        addBookStage.setTitle("Add Book");

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 15; -fx-alignment: center;");

        TextField titleField = new TextField();
        titleField.setPromptText("Book Title");

        TextField authorField = new TextField();
        authorField.setPromptText("Author");

        Button submitButton = new Button("Add Book");
        submitButton.setOnAction(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            if (!title.isEmpty() && !author.isEmpty()) {
                Book newBook = new Book(title, author, currentUser.getUsername());
                books.add(newBook);
                saveBooks();
                System.out.println("Book added: " + newBook);
                addBookStage.close();
            } else {
                showAlert("Please fill in all fields!");
            }
        });

        layout.getChildren().addAll(new Label("Add a New Book"), titleField, authorField, submitButton);

        Scene scene = new Scene(layout, 300, 200);
        addBookStage.setScene(scene);
        addBookStage.showAndWait();
    }

    /**
     * Метод отображения книг текущего пользователя.
     */
    private void showMyBooks() {
        Stage myBooksStage = new Stage();
        myBooksStage.setTitle("My Books");

        ListView<String> myBooksListView = new ListView<>();
        books.stream()
                .filter(book -> book.getOwner().equals(currentUser.getUsername()))
                .forEach(book -> myBooksListView.getItems().add(book.toString()));

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 15;");
        layout.getChildren().addAll(new Label("Your Books:"), myBooksListView);

        Scene scene = new Scene(layout, 300, 250);
        myBooksStage.setScene(scene);
        myBooksStage.show();
    }

    /**
     * Метод отображения всех книг.
     */
    private void showBookList() {
        Stage bookListStage = new Stage();
        bookListStage.setTitle("All Books");

        ListView<String> bookListView = new ListView<>();
        books.forEach(book -> bookListView.getItems().add(book.toString()));

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 15;");
        layout.getChildren().addAll(new Label("Books Available:"), bookListView);

        Scene scene = new Scene(layout, 300, 250);
        bookListStage.setScene(scene);
        bookListStage.show();
    }

    private void showRegisterUserForm() {
        showAlert("Registration form not implemented yet.");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Вспомогательный класс для представления книги.
     */
    static class Book {
        private String title;
        private String author;
        private String owner;

        public Book() {}

        public Book(String title, String author, String owner) {
            this.title = title;
            this.author = author;
            this.owner = owner;
        }

        public String getOwner() {
            return owner;
        }

        @Override
        public String toString() {
            return title + " by " + author + " (Owner: " + owner + ")";
        }
    }
}
