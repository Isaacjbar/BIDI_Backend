-- Insertions for `book` table
INSERT INTO `book` (`author`, `copies`, `description`, `status`, `title`) VALUES
('George Orwell', 10, 'Dystopian novel about a totalitarian regime', 'ACTIVE', '1984'),
('J.K. Rowling', 15, 'A young wizard’s adventures', 'ACTIVE', 'Harry Potter and the Philosopher''s Stone'),
('J.R.R. Tolkien', 8, 'A hobbit''s journey to destroy a powerful ring', 'ACTIVE', 'The Lord of the Rings'),
('F. Scott Fitzgerald', 5, 'A critique of the American Dream', 'INACTIVE', 'The Great Gatsby'),
('Jane Austen', 12, 'A story of love and social standing', 'ACTIVE', 'Pride and Prejudice');

-- Insertions for `category` table
INSERT INTO `category` (`category_name`, `status`) VALUES
('Science Fiction', 'ACTIVE'),
('Fantasy', 'ACTIVE'),
('Classics', 'ACTIVE'),
('Romance', 'ACTIVE'),
('Non-Fiction', 'INACTIVE');

-- Insertions for `book_category` table
INSERT INTO `book_category` (`category_id`, `book_id`) VALUES
(1, 1), -- Book '1984' in 'Science Fiction'
(2, 2), -- Book 'Harry Potter and the Philosopher''s Stone' in 'Fantasy'
(2, 3), -- Book 'The Lord of the Rings' in 'Fantasy'
(3, 4), -- Book 'The Great Gatsby' in 'Classics'
(4, 5); -- Book 'Pride and Prejudice' in 'Romance'

-- Insertions for `loan` table
INSERT INTO `loan` (`loan_date`, `due_date`, `status`, `book_id`, `user_id`) VALUES
('2024-11-01 10:00:00', '2024-11-11 10:00:00', 'ACTIVE', 1, 1),
('2024-11-02 11:30:00', '2024-11-12 11:30:00', 'ACTIVE', 2, 2),
('2024-11-03 14:00:00', '2024-11-13 14:00:00', 'INACTIVE', 3, 3),
('2024-11-04 15:00:00', '2024-11-14 15:00:00', 'ACTIVE', 4, 1),
('2024-11-05 16:00:00', '2024-11-15 16:00:00', 'ACTIVE', 5, 2);
