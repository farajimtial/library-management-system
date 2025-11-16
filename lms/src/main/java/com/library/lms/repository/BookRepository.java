package com.library.lms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.library.lms.entity.Book;
import com.library.lms.enums.BookStatus;

/**
 * BookRepository Interface
 * Handles database operations for Book entity
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    // Find book by ISBN
    Optional<Book> findByIsbn(String isbn);
    
    // Find books by title (partial match, case-insensitive)
    List<Book> findByTitleContainingIgnoreCase(String title);
    
    // Find books by author (partial match, case-insensitive)
    List<Book> findByAuthorContainingIgnoreCase(String author);
    
    // Find books by category
    List<Book> findByCategory(String category);
    
    // Find books by status
    List<Book> findByStatus(BookStatus status);
    
    // Find available books
    @Query("SELECT b FROM Book b WHERE b.availableCopies > 0")
    List<Book> findAvailableBooks();
    
    // Search books by multiple criteria
    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Book> searchBooks(@Param("keyword") String keyword);
    
    // Check if ISBN exists
    boolean existsByIsbn(String isbn);
}

