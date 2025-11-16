// ============================================
// ReturnRequestDTO.java
// ============================================
package com.library.lms.dto;

/**
 * ReturnRequestDTO - Data Transfer Object for Return Requests
 * Used when a student returns a borrowed book
 */
public class ReturnRequestDTO {
    private Long recordId;
    private Long studentId;
    private Long bookId;
    
    // Constructors
    public ReturnRequestDTO() {}
    
    public ReturnRequestDTO(Long recordId, Long studentId, Long bookId) {
        this.recordId = recordId;
        this.studentId = studentId;
        this.bookId = bookId;
    }
    
    // Getters and Setters
    public Long getRecordId() {
        return recordId;
    }
    
    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }
    
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
}
