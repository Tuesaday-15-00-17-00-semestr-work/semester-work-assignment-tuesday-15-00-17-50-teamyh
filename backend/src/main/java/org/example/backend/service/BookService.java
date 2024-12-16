package org.example.backend.service;

import org.example.backend.model.Book;
import org.example.backend.model.User;
import org.example.backend.repository.BookRepository;
import org.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookService(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieve all books in the system.
     *
     * @return List of books
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Add a new book to the system and associate it with the user.
     *
     * @param book     Book object to add
     * @param username Username of the user adding the book
     */
    public void addBook(Book book, String username) {
        User user = findUserByUsername(username);
        book.setAddedBy(user);
        bookRepository.save(book);
    }

    /**
     * Retrieve books added by a specific user.
     *
     * @param username Username of the user
     * @return List of books added by the user
     */
    public List<Book> getBooksByUser(String username) {
        User user = findUserByUsername(username);
        return bookRepository.findByAddedBy(user);
    }

    /**
     * Helper method to find a user by username.
     */
    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User with username " + username + " not found"));
    }
}
