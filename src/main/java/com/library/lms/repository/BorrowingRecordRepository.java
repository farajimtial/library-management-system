package com.library.lms.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.library.lms.entity.Book;
import com.library.lms.entity.BorrowingRecord;
import com.library.lms.entity.Student;
import com.library.lms.enums.BorrowStatus;

/**
 * BorrowingRecordRepository Interface
 * Handles database operations for BorrowingRecord entity
 */
@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    
    // Find all borrowing records by student
    List<BorrowingRecord> findByStudent(Student student);
    
    // Find active borrowing records by student
    List<BorrowingRecord> findByStudentAndStatus(Student student, BorrowStatus status);
    
    // Find borrowing records by book
    List<BorrowingRecord> findByBook(Book book);
    
    // Find borrowing records by status
    List<BorrowingRecord> findByStatus(BorrowStatus status);
    
    // Find overdue records
    @Query("SELECT br FROM BorrowingRecord br WHERE " +
           "br.status = 'ACTIVE' AND br.dueDate < :currentDate")
    List<BorrowingRecord> findOverdueRecords(@Param("currentDate") LocalDate currentDate);
    
    // Find active borrowings for a student
    @Query("SELECT br FROM BorrowingRecord br WHERE " +
           "br.student.userId = :studentId AND br.status = 'ACTIVE'")
    List<BorrowingRecord> findActiveBorrowingsByStudent(@Param("studentId") Long studentId);
    
    // Count active borrowings for a student
    @Query("SELECT COUNT(br) FROM BorrowingRecord br WHERE " +
           "br.student.userId = :studentId AND br.status = 'ACTIVE'")
    Long countActiveBorrowingsByStudent(@Param("studentId") Long studentId);
    
    // Find borrowing history by student and book
    List<BorrowingRecord> findByStudentAndBook(Student student, Book book);
}
