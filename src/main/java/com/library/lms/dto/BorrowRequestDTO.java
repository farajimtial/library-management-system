// ============================================
// BorrowRequestDTO.java
// ============================================
package com.library.lms.dto;

/**
 * BorrowRequestDTO - Data Transfer Object for Borrow Requests
 * Used when a student requests to borrow a book
 */
public class BorrowRequestDTO {
    private Long studentId;
    private Long bookId;
    private Integer borrowDays; // Number of days to borrow
    
    // Constructors
    public BorrowRequestDTO() {}
    
    public BorrowRequestDTO(Long studentId, Long bookId, Integer borrowDays) {
        this.studentId = studentId;
        this.bookId = bookId;
        this.borrowDays = borrowDays;
    }
    
    // Getters and Setters
    public Long getStudentId() {
        return studentId;
    }
    
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
    
    public Long getBookId() {
        return bookId;
    }
    
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    
    public Integer getBorrowDays() {
        return borrowDays;
    }
    
    public void setBorrowDays(Integer borrowDays) {
        this.borrowDays = borrowDays;
    }
}
