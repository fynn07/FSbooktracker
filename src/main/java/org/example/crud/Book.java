package org.example.crud;

public class Book {
    private String bookName;
    private String bookAuthor;
    private String bookPages;

    // Constructor
    public Book(String bookName, String bookAuthor, String bookPages) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookPages = bookPages;
    }

    // Getters and setters for the attributes

    public String getBookName(){
        return bookName;
    }

    public String getBookAuthor(){
        return bookAuthor;
    }

    public String getBookPages(){
        return bookPages;
    }

}
