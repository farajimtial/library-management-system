// ============================================
// LibrarianRepository.java
// ============================================
package com.library.lms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.lms.entity.Librarian;

/**
 * LibrarianRepository Interface
 * Handles database operations for Librarian entity
 */
@Repository
public interface LibrarianRepository extends JpaRepository<Librarian, Long> {
    
    // Find librarian by librarianId
    Optional<Librarian> findByLibrarianId(String librarianId);
    
    // Find librarian by employeeId
    Optional<Librarian> findByEmployeeId(String employeeId);
    
    // Find librarian by username
    Optional<Librarian> findByUsername(String username);
    
    // Check if librarianId exists
    boolean existsByLibrarianId(String librarianId);
    
    // Check if employeeId exists
    boolean existsByEmployeeId(String employeeId);
}
