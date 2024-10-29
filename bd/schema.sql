-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema library_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema library_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `library_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `library_db` ;

-- -----------------------------------------------------
-- Table `library_db`.`book`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_db`.`book` (
  `book_id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `author` VARCHAR(255) NOT NULL,
  `status` ENUM('active', 'inactive') NOT NULL DEFAULT 'active',
  `description` VARCHAR(250) NULL DEFAULT NULL,
  PRIMARY KEY (`book_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `library_db`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_db`.`category` (
  `category_id` INT NOT NULL AUTO_INCREMENT,
  `category_name` VARCHAR(100) NOT NULL,
  `status` ENUM('active', 'inactive') NULL DEFAULT 'active',
  PRIMARY KEY (`category_id`),
  UNIQUE INDEX `category_name` (`category_name` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `library_db`.`book_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_db`.`book_category` (
  `book_id` INT NOT NULL,
  `category_id` INT NOT NULL,
  PRIMARY KEY (`book_id`, `category_id`),
  INDEX `category_id` (`category_id` ASC) VISIBLE,
  CONSTRAINT `book_category_ibfk_1`
    FOREIGN KEY (`book_id`)
    REFERENCES `library_db`.`book` (`book_id`),
  CONSTRAINT `book_category_ibfk_2`
    FOREIGN KEY (`category_id`)
    REFERENCES `library_db`.`category` (`category_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `library_db`.`inventory`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_db`.`inventory` (
  `inventory_id` INT NOT NULL AUTO_INCREMENT,
  `book_id` INT NULL DEFAULT NULL,
  `available_copies` INT NOT NULL,
  `total_copies` INT NOT NULL,
  `status` ENUM('active', 'inactive') NULL DEFAULT 'active',
  PRIMARY KEY (`inventory_id`),
  INDEX `book_id` (`book_id` ASC) VISIBLE,
  CONSTRAINT `inventory_ibfk_1`
    FOREIGN KEY (`book_id`)
    REFERENCES `library_db`.`book` (`book_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `library_db`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_db`.`user` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `role` ENUM('administrator', 'client', 'guest') NOT NULL,
  `status` ENUM('active', 'inactive') NULL DEFAULT 'active',
  `phone_number` VARCHAR(15) NULL DEFAULT NULL,
  `code` VARCHAR(255) NULL DEFAULT NULL,
  `code_generated_at` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `email` (`email` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `library_db`.`loan`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_db`.`loan` (
  `loan_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NULL DEFAULT NULL,
  `inventory_id` INT NULL DEFAULT NULL,
  `loan_date` DATE NOT NULL,
  `due_date` DATE NOT NULL,
  `return_date` DATE NULL DEFAULT NULL,
  `status` ENUM('pending', 'returned') NULL DEFAULT 'pending',
  PRIMARY KEY (`loan_id`),
  INDEX `user_id` (`user_id` ASC) VISIBLE,
  INDEX `inventory_id` (`inventory_id` ASC) VISIBLE,
  CONSTRAINT `loan_ibfk_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `library_db`.`user` (`user_id`),
  CONSTRAINT `loan_ibfk_2`
    FOREIGN KEY (`inventory_id`)
    REFERENCES `library_db`.`inventory` (`inventory_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

USE `library_db`;

DELIMITER $$
USE `library_db`$$
CREATE
DEFINER=`root`@`localhost`
TRIGGER `library_db`.`delete_recovery_code`
BEFORE UPDATE ON `library_db`.`user`
FOR EACH ROW
BEGIN
    -- Eliminar el código de recuperación si han pasado más de 24 horas desde que fue asignado
    IF (NEW.code IS NOT NULL AND OLD.code IS NOT NULL AND TIMESTAMPDIFF(HOUR, OLD.code_generated_at, NOW()) > 24) THEN
        SET NEW.code = NULL;
        SET NEW.code_generated_at = NULL;
    END IF;
END$$

USE `library_db`$$
CREATE
DEFINER=`root`@`localhost`
TRIGGER `library_db`.`after_loan_insert`
AFTER INSERT ON `library_db`.`loan`
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
END$$

USE `library_db`$$
CREATE
DEFINER=`root`@`localhost`
TRIGGER `library_db`.`after_loan_update`
AFTER UPDATE ON `library_db`.`loan`
FOR EACH ROW
BEGIN
    -- Si el estado del préstamo cambia a 'returned', devolver una copia al inventario
    IF NEW.status = 'returned' THEN
        UPDATE inventory
        SET available_copies = available_copies + 1
        WHERE inventory_id = NEW.inventory_id;
    END IF;
END$$


DELIMITER ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
