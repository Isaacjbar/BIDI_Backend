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
    status ENUM('active', 'inactive') DEFAULT 'active',
    phone_number VARCHAR(15), -- Nueva columna para el número de teléfono
    code VARCHAR(255),
    code_generated_at TIMESTAMP NULL DEFAULT NULL -- Columna para almacenar el tiempo de generación del código
);


-- Crear tabla category
CREATE TABLE category (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(100) UNIQUE NOT NULL,
    status ENUM('active', 'inactive') DEFAULT 'active'
);

-- Crear tabla book (sin available_copies para evitar duplicidad)
CREATE TABLE book (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(13) NOT NULL,
    publication_date DATE NOT NULL,
    publisher VARCHAR(255) NOT NULL,
    edition VARCHAR(50) NOT NULL,
    number_of_pages INT,
    status ENUM('active', 'inactive') DEFAULT 'active',
    UNIQUE (isbn)
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

-- Crear tabla book_category
CREATE TABLE book_category (
    book_id INT,
    category_id INT,
    PRIMARY KEY (book_id, category_id),
    FOREIGN KEY (book_id) REFERENCES book(book_id),
    FOREIGN KEY (category_id) REFERENCES category(category_id)
);

-- Crear tabla loan (relacionando directamente con inventory)
CREATE TABLE loan (
    loan_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    inventory_id INT,  -- Relaciona con inventory en lugar de directamente con book
    loan_date DATE NOT NULL,
    due_date DATE NOT NULL, -- Fecha de devolución esperada
    return_date DATE,
    status ENUM('pending', 'returned') DEFAULT 'pending',
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (inventory_id) REFERENCES inventory(inventory_id)
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
    WHERE inventory_id = NEW.inventory_id AND available_copies > 0;

    -- Lanzar error si no hay copias disponibles
    IF (SELECT available_copies FROM inventory WHERE inventory_id = NEW.inventory_id) < 0 THEN
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
        WHERE inventory_id = NEW.inventory_id;
    END IF;
END //

DELIMITER ;

-- Crear trigger para eliminar el código de recuperación después de 24 horas
DELIMITER //

CREATE TRIGGER delete_recovery_code
BEFORE UPDATE ON user
FOR EACH ROW
BEGIN
    -- Eliminar el código de recuperación si han pasado más de 24 horas desde que fue asignado
    IF (NEW.code IS NOT NULL AND OLD.code IS NOT NULL AND TIMESTAMPDIFF(HOUR, OLD.code_generated_at, NOW()) > 24) THEN
        SET NEW.code = NULL;
        SET NEW.code_generated_at = NULL;
    END IF;
END //

DELIMITER ;
