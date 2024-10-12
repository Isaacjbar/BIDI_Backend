-- Insertar datos de prueba en la tabla user con contraseñas hasheadas usando SHA-256
INSERT INTO user (name, email, password, role, status) VALUES
('Canchola Aguilar Alan Yahir', 'alan.canchola@example.com', SHA2('password1', 256), 'administrator', 'active'),
('Apaez Sotelo Alexis Jesus', 'alexis.apaez@example.com', SHA2('password2', 256), 'administrator', 'active'),
('Jimenez Barcelata Isaac', 'isaac.jimenez@example.com', SHA2('password3', 256), 'administrator', 'active'),
('Negrete Juarez Vanessa', 'vanessa.negrete@example.com', SHA2('password4', 256), 'administrator', 'active'),
('Ramirez Lopez Alicia Fernanda', 'alicia.ramirez@example.com', SHA2('password5', 256), 'administrator', 'active'),
('Saldaña Villamil Bryan Jesus', 'bryan.saldaña@example.com', SHA2('password6', 256), 'administrator', 'active');

-- Insertar datos de prueba en la tabla category
INSERT INTO category (category_name, status) VALUES
('Fiction', 'active'),
('Non-fiction', 'active'),
('Science', 'active'),
('Technology', 'active');

-- Insertar datos de prueba en la tabla book
INSERT INTO book (title, author, isbn, publication_date, status) VALUES
('The Science of Space', 'Carl Sagan', '9780131103627', '1980-11-15', 'active'),
('Artificial Intelligence', 'Stuart Russell', '9780136042594', '2010-06-23', 'active'),
('Digital Fortress', 'Dan Brown', '9780425195985', '2004-01-01', 'active'),
('Introduction to Algorithms', 'Thomas H. Cormen', '9780262033848', '2009-07-31', 'active');

-- Insertar datos de prueba en la tabla inventory
INSERT INTO inventory (book_id, available_copies, total_copies, status) VALUES
(1, 5, 5, 'active'),
(2, 3, 3, 'active'),
(3, 7, 7, 'active'),
(4, 10, 10, 'active');

-- Insertar datos de prueba en la tabla book_category (relación entre books y categories)
INSERT INTO book_category (book_id, category_id) VALUES
(1, 3), -- 'The Science of Space' pertenece a 'Science'
(2, 4), -- 'Artificial Intelligence' pertenece a 'Technology'
(3, 1), -- 'Digital Fortress' pertenece a 'Fiction'
(4, 4); -- 'Introduction to Algorithms' pertenece a 'Technology'

-- Insertar datos de prueba en la tabla loan
INSERT INTO loan (user_id, book_id, loan_date, due_date, status) VALUES
(1, 1, '2024-10-01', '2024-10-15', 'pending'), -- Canchola Aguilar Alan Yahir toma 'The Science of Space'
(2, 2, '2024-10-03', '2024-10-17', 'pending'), -- Apaez Sotelo Alexis Jesus toma 'Artificial Intelligence'
(3, 3, '2024-10-02', '2024-10-16', 'pending'), -- Jimenez Barcelata Isaac toma 'Digital Fortress'
(4, 4, '2024-10-04', '2024-10-18', 'pending'); -- Negrete Juarez Vanessa toma 'Introduction to Algorithms'
