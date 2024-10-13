package com.sibi.GestionDeBibliotecas.Libro.Model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "book")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, unique = true, length = 13)
    private String isbn;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date publicationDate;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false, length = 50)
    private String edition;

    @Column
    private Integer numberOfPages;

    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private Status status = Status.ACTIVE;

    public enum Status {
        ACTIVE,
        INACTIVE
    }

    public Libro() {

    }

    public Libro(Integer bookId, String title, String author, String isbn, Date publicationDate, String publisher, String edition, Integer numberOfPages, Status status) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.publisher = publisher;
        this.edition = edition;
        this.numberOfPages = numberOfPages;
        this.status = status;
    }

    public Libro(String title, String author, String isbn, Date publicationDate, String publisher, String edition, Integer numberOfPages, Status status) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.publisher = publisher;
        this.edition = edition;
        this.numberOfPages = numberOfPages;
        this.status = status;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}