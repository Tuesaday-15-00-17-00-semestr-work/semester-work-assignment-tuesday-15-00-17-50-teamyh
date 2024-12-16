CREATE TABLE patrons (
                         patron_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         phone_number VARCHAR(20)
);

