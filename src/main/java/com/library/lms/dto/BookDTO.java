// ============================================
// BookDTO.java
// ============================================
package com.library.lms.dto;

import com.library.lms.enums.BookStatus;

/**
 * BookDTO - Data Transfer Object for Book
 * Used to transfer book data between layers
 */
public class BookDTO {
    private Long bookId;
    private String isbn;
    private String title;
    private String author;
    private String category;
    private Integer publicationYear;
    private Integer totalCopies;
    private Integer availableCopies;
    private BookStatus status;
    
    // Constructors
    public BookDTO() {}
    
    public BookDTO(Long bookId, String isbn, String title, String author, 
                   String category, Integer publicationYear, Integer totalCopies, 
                   Integer availableCopies, BookStatus status) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.publicationYear = publicationYear;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.status = status;
    }
    
    // Getters and Setters
    public Long getBookId() {
        return bookId;
    }
    
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
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
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public Integer getPublicationYear() {
        return publicationYear;
    }
    
    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }
    
    public Integer getTotalCopies() {
        return totalCopies;
    }
    
    public void setTotalCopies(Integer totalCopies) {
        this.totalCopies = totalCopies;
    }
    
    public Integer getAvailableCopies() {
        return availableCopies;
    }
    
    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }
    
    public BookStatus getStatus() {
        return status;
    }
    
    public void setStatus(BookStatus status) {
        this.status = status;
    }
}

