package com.library.lms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

/**
 * Librarian Entity - Extends User
 * Represents librarians in the library system
 */
@Entity
@Table(name = "librarians")
@PrimaryKeyJoinColumn(name = "user_id")
public class Librarian extends User {
    
    @Column(unique = true, length = 20)
    private String librarianId;
    
    @Column(unique = true, length = 20)
    private String employeeId;
    
    // Constructors
    public Librarian() {
        super();
    }
    
    public Librarian(String username, String password, String email, 
                     String librarianId, String employeeId) {
        super(username, password, email, com.library.lms.enums.Role.LIBRARIAN);
        this.librarianId = librarianId;
        this.employeeId = employeeId;
    }
    
    // Getters and Setters
    public String getLibrarianId() {
        return librarianId;
    }
    
    public void setLibrarianId(String librarianId) {
        this.librarianId = librarianId;
    }
    
    public String getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    
    @Override
    public String toString() {
        return "Librarian{" +
                "librarianId='" + librarianId + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", " + super.toString() +
                '}';
    }
}
