package org.example.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Title cannot be null")
    @Size(min = 1, message = "Title must not be empty")
    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Связь с User (обязательно)
    private User addedBy; // Владелец книги

    @ManyToOne
    @JoinColumn(name = "patron_id") // Связь с Patron (необязательно)
    private Patron patron; // Текущий арендатор/пользователь книги

    // Конструктор по умолчанию (нужен для JPA)
    public Book() {
    }

    // Полный конструктор
    public Book(String title, User addedBy) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be null or empty");
        }
        if (addedBy == null) {
            throw new IllegalArgumentException("Book must have an owner (User)");
        }

        this.title = title;
        this.addedBy = addedBy;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be null or empty");
        }
        this.title = title;
    }

    public User getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(User addedBy) {
        if (addedBy == null) {
            throw new IllegalArgumentException("Book must have an owner (User)");
        }
        this.addedBy = addedBy;
    }

    public Patron getPatron() {
        return patron;
    }

    public void setPatron(Patron patron) {
        this.patron = patron;
    }

    // Переопределение toString для удобного отображения
    @Override
    public String toString() {
        return String.format("%s (Owner: %s)",
                title,
                addedBy != null ? addedBy.getUsername() : "Unknown User");
    }
}
