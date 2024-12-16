package org.example.backend.repository;

import org.example.backend.model.Book;
import org.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAddedBy(User user); // Книги, добавленные пользователем
    List<Book> findByPatronId(Long patronId); // Книги, связанные с арендатором
}
