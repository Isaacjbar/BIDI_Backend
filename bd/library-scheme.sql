-- Crear base de datos
CREATE DATABASE library_db;
USE library_db;

-- Crear tabla user
CREATE TABLE user (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('administrator', 'client', 'guest') NOT NULL,
    status ENUM('active', 'inactive') DEFAULT 'active'
);

-- Crear tabla category
CREATE TABLE category (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(100) UNIQUE NOT NULL,
    status ENUM('active', 'inactive') DEFAULT 'active'
);

-- Crear tabla book
CREATE TABLE book (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,            -- Título del libro (Alfanumérico)
    author VARCHAR(255) NOT NULL,           -- Autor (Alfanumérico)
    isbn VARCHAR(13) NOT NULL,              -- ISBN único
    publication_date DATE NOT NULL,         -- Fecha de publicación del libro
    publisher VARCHAR(255) NOT NULL,        -- Editorial (Alfanumérico)
    edition VARCHAR(50) NOT NULL,           -- Edición (Alfanumérico)
    available_copies INT NOT NULL,          -- Número de copias disponibles (Numérico)
    number_of_pages INT,                    -- Número de páginas (Numérico, opcional)
    status ENUM('active', 'inactive') DEFAULT 'active', -- Estado del libro (activo/inactivo)
    UNIQUE (isbn)                           -- ISBN debe ser único
);


-- Crear tabla inventory
CREATE TABLE inventory (
    inventory_id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT,
    available_copies INT NOT NULL,
    total_copies INT NOT NULL,
    status ENUM('active', 'inactive') DEFAULT 'active',
    FOREIGN KEY (book_id) REFERENCES book(book_id)
);

-- Crear tabla book_category (relación muchos a muchos entre books y categories)
CREATE TABLE book_category (
    book_id INT,
    category_id INT,
    PRIMARY KEY (book_id, category_id),
    FOREIGN KEY (book_id) REFERENCES book(book_id),
    FOREIGN KEY (category_id) REFERENCES category(category_id)
);

-- Crear tabla loan
CREATE TABLE loan (
    loan_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    book_id INT,
    loan_date DATE NOT NULL,
    due_date DATE NOT NULL, -- Fecha de devolución esperada
    return_date DATE,
    status ENUM('pending', 'returned') DEFAULT 'pending',
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (book_id) REFERENCES book(book_id)
);

-- Crear trigger para descontar inventario cuando se inserta un préstamo
DELIMITER //

CREATE TRIGGER after_loan_insert
AFTER INSERT ON loan
FOR EACH ROW
BEGIN
    -- Reducir una copia del libro prestado si hay copias disponibles
    UPDATE inventory
    SET available_copies = available_copies - 1
    WHERE book_id = NEW.book_id AND available_copies > 0;

    -- Lanzar error si no hay copias disponibles
    IF (SELECT available_copies FROM inventory WHERE book_id = NEW.book_id) < 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No available copies for this book';
    END IF;
END //

DELIMITER ;

-- Crear trigger para restaurar inventario cuando se devuelve un préstamo
DELIMITER //

CREATE TRIGGER after_loan_update
AFTER UPDATE ON loan
FOR EACH ROW
BEGIN
    -- Si el estado del préstamo cambia a 'returned', devolver una copia al inventario
    IF NEW.status = 'returned' THEN
        UPDATE inventory
        SET available_copies = available_copies + 1
        WHERE book_id = NEW.book_id;
    END IF;
END //

DELIMITER ;
