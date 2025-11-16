package com.library.lms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.lms.entity.Librarian;
import com.library.lms.entity.Student;
import com.library.lms.entity.User;
import com.library.lms.enums.Role;
import com.library.lms.repository.LibrarianRepository;
import com.library.lms.repository.StudentRepository;
import com.library.lms.repository.UserRepository;

/**
 * UserService - Business logic for User operations
 * Handles user authentication, registration, and management
 */
@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private LibrarianRepository librarianRepository;
    
    /**
     * Authenticate user with username and password
     */
    public User login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Simple password check (no encryption as per requirements)
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Register a new student
     */
    public Student registerStudent(Student student) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(student.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (studentRepository.existsByStudentId(student.getStudentId())) {
            throw new RuntimeException("Student ID already exists");
        }
        
        student.setRole(Role.STUDENT);
        return studentRepository.save(student);
    }
    
    /**
     * Register a new librarian (only accessible by admin)
     */
    public Librarian registerLibrarian(Librarian librarian) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(librarian.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(librarian.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (librarianRepository.existsByLibrarianId(librarian.getLibrarianId())) {
            throw new RuntimeException("Librarian ID already exists");
        }
        
        librarian.setRole(Role.LIBRARIAN);
        return librarianRepository.save(librarian);
    }
    
    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Get user by ID
     */
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }
    
    /**
     * Get user by username
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * Get all students
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    /**
     * Get all librarians
     */
    public List<Librarian> getAllLibrarians() {
        return librarianRepository.findAll();
    }
    
    /**
     * Delete user by ID
     */
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
    
    /**
     * Update user information
     */
    public User updateUser(User user) {
        return userRepository.save(user);
    }
}