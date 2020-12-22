package com.example.books.models;

public class BookModel {
    String content, description, bookCover, title, id;

    public BookModel(){

    }

    public BookModel(String content, String description, String bookCover, String title, String id) {
        this.content = content;
        this.description = description;
        this.bookCover = bookCover;
        this.title = title;
        this.id = id;

    }

    public String getContent() {
        return content;
    }
    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getBookCover() {
        return bookCover;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
