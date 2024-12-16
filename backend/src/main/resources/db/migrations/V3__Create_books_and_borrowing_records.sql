CREATE TABLE books (
                       book_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255) NOT NULL,
                       publication_year INT NOT NULL,
                       isbn VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE borrowingrecord (
                                 borrowing_record_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 book_id BIGINT NOT NULL,
                                 patron_id BIGINT NOT NULL,
                                 contact_info VARCHAR(255),
                                 FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
                                 FOREIGN KEY (patron_id) REFERENCES users(user_id) ON DELETE CASCADE
);
