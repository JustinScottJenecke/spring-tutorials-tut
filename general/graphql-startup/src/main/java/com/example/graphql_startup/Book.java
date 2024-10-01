package com.example.graphql_startup;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;

    public Book() {}

    public Book(String titlei, String publisheri, Author authori) {
        title = titlei;
        publisher = publisheri;
        author = authori;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(title, book.title) && Objects.equals(publisher, book.publisher) && Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, publisher, author);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", publisher='" + publisher + '\'' +
                ", author=" + author +
                '}';
    }
}
