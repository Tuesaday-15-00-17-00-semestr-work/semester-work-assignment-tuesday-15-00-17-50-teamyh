package org.example.backend.controller;

import org.example.backend.model.Book;
import org.example.backend.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Получить список всех книг
     */
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    /**
     * Добавить новую книгу (для зарегистрированного пользователя)
     */
    @PostMapping
    public ResponseEntity<String> addBook(@RequestBody Book book, @RequestParam String username) {
        try {
            bookService.addBook(book, username);
            return ResponseEntity.ok("Book added successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Получить книги конкретного пользователя
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<List<Book>> getBooksByUser(@PathVariable String username) {
        try {
            return ResponseEntity.ok(bookService.getBooksByUser(username));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
