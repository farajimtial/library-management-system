package com.library.lms.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.lms.entity.Book;
import com.library.lms.entity.BorrowingRecord;
import com.library.lms.entity.Student;
import com.library.lms.enums.BorrowStatus;
import com.library.lms.repository.BookRepository;
import com.library.lms.repository.BorrowingRecordRepository;
import com.library.lms.repository.StudentRepository;

/**
 * BorrowingService - Business logic for Borrowing operations
 * Handles book borrowing, returning, and record management
 */
@Service
@Transactional
public class BorrowingService {
    
    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private BookService bookService;
    
    /**
     * Borrow a book
     */
    public BorrowingRecord borrowBook(Long studentId, Long bookId, Integer borrowDays) {
        // Get student
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            throw new RuntimeException("Student not found");
        }
        Student student = studentOpt.get();
        
        // Get book
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isEmpty()) {
            throw new RuntimeException("Book not found");
        }
        Book book = bookOpt.get();
        
        // Check if book is available
        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("Book is not available for borrowing");
        }
        
        // Create borrowing record
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(borrowDays != null ? borrowDays : 14); // Default 14 days
        
        BorrowingRecord record = new BorrowingRecord(student, book, borrowDate, dueDate);
        record.setStatus(BorrowStatus.ACTIVE);
        
        // Update book availability
        bookService.updateBookAvailability(bookId, -1);
        
        // Update student's total borrowed books
        student.setTotalBorrowedBooks(student.getTotalBorrowedBooks() + 1);
        studentRepository.save(student);
        
        return borrowingRecordRepository.save(record);
    }
    
    /**
     * Return a book
     */
    public BorrowingRecord returnBook(Long recordId) {
        // Get borrowing record
        Optional<BorrowingRecord> recordOpt = borrowingRecordRepository.findById(recordId);
        if (recordOpt.isEmpty()) {
            throw new RuntimeException("Borrowing record not found");
        }
        BorrowingRecord record = recordOpt.get();
        
        // Check if already returned
        if (record.getStatus() == BorrowStatus.RETURNED) {
            throw new RuntimeException("Book already returned");
        }
        
        // Update record
        record.setReturnDate(LocalDate.now());
        record.setStatus(BorrowStatus.RETURNED);
        
        // Update book availability
        bookService.updateBookAvailability(record.getBook().getBookId(), 1);
        
        return borrowingRecordRepository.save(record);
    }
    
    /**
     * Get all borrowing records
     */
    public List<BorrowingRecord> getAllBorrowingRecords() {
        return borrowingRecordRepository.findAll();
    }
    
    /**
     * Get borrowing records by student
     */
    public List<BorrowingRecord> getBorrowingRecordsByStudent(Long studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isPresent()) {
            return borrowingRecordRepository.findByStudent(studentOpt.get());
        }
        return List.of();
    }
    
    /**
     * Get active borrowings by student
     */
    public List<BorrowingRecord> getActiveBorrowingsByStudent(Long studentId) {
        return borrowingRecordRepository.findActiveBorrowingsByStudent(studentId);
    }
    
    /**
     * Get overdue records
     */
    public List<BorrowingRecord> getOverdueRecords() {
        List<BorrowingRecord> overdueRecords = borrowingRecordRepository.findOverdueRecords(LocalDate.now());
        
        // Update status to OVERDUE
        for (BorrowingRecord record : overdueRecords) {
            if (record.getStatus() == BorrowStatus.ACTIVE) {
                record.setStatus(BorrowStatus.OVERDUE);
                borrowingRecordRepository.save(record);
            }
        }
        
        return overdueRecords;
    }
    
    /**
     * Get borrowing record by ID
     */
    public Optional<BorrowingRecord> getBorrowingRecordById(Long recordId) {
        return borrowingRecordRepository.findById(recordId);
    }
}