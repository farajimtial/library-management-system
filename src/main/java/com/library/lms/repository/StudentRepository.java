// ============================================
// StudentRepository.java
// ============================================
package com.library.lms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.lms.entity.Student;

/**
 * StudentRepository Interface
 * Handles database operations for Student entity
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    // Find student by studentId
    Optional<Student> findByStudentId(String studentId);
    
    // Find students by department
    List<Student> findByDepartment(String department);
    
    // Find students by semester
    List<Student> findBySemester(Integer semester);
    
    // Find student by username
    Optional<Student> findByUsername(String username);
    
    // Check if studentId exists
    boolean existsByStudentId(String studentId);
}
