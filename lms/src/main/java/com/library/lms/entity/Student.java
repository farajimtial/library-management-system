package com.library.lms.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Student Entity - Extends User
 * Represents students in the library system
 */
@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "user_id")
public class Student extends User {
    
    @Column(unique = true, length = 20)
    private String studentId;
    
    @Column(length = 100)
    private String department;
    
    private Integer semester;
    
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer totalBorrowedBooks = 0;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BorrowingRecord> borrowingRecords = new ArrayList<>();
    
    // Constructors
    public Student() {
        super();
    }
    
    public Student(String username, String password, String email, String studentId, 
                   String department, Integer semester) {
        super(username, password, email, com.library.lms.enums.Role.STUDENT);
        this.studentId = studentId;
        this.department = department;
        this.semester = semester;
        this.totalBorrowedBooks = 0;
    }
    
    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public Integer getSemester() {
        return semester;
    }
    
    public void setSemester(Integer semester) {
        this.semester = semester;
    }
    
    public Integer getTotalBorrowedBooks() {
        return totalBorrowedBooks;
    }
    
    public void setTotalBorrowedBooks(Integer totalBorrowedBooks) {
        this.totalBorrowedBooks = totalBorrowedBooks;
    }
    
    public List<BorrowingRecord> getBorrowingRecords() {
        return borrowingRecords;
    }
    
    public void setBorrowingRecords(List<BorrowingRecord> borrowingRecords) {
        this.borrowingRecords = borrowingRecords;
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", department='" + department + '\'' +
                ", semester=" + semester +
                ", totalBorrowedBooks=" + totalBorrowedBooks +
                ", " + super.toString() +
                '}';
    }
}