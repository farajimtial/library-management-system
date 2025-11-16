// ============================================
// BookService.java
// ============================================
package com.library.lms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.lms.entity.Book;
import com.library.lms.enums.BookStatus;
import com.library.lms.repository.BookRepository;

/**
 * BookService - Business logic for Book operations
 * Handles book CRUD operations and search functionality
 */
@Service
@Transactional
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    /**
     * Add a new book to the library
     */
    public Book addBook(Book book) {
        // Check if ISBN already exists
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new RuntimeException("Book with ISBN " + book.getIsbn() + " already exists");
        }
        
        // Set initial values
        book.setAvailableCopies(book.getTotalCopies());
        book.setStatus(BookStatus.AVAILABLE);
        
        return bookRepository.save(book);
    }
    
    /**
     * Update an existing book
     */
    public Book updateBook(Book book) {
        Optional<Book> existingBookOpt = bookRepository.findById(book.getBookId());
        if (existingBookOpt.isEmpty()) {
            throw new RuntimeException("Book not found");
        }
        
        return bookRepository.save(book);
    }
    
    /**
     * Delete a book by ID
     */
    public void deleteBook(Long bookId) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isEmpty()) {
            throw new RuntimeException("Book not found");
        }
        
        bookRepository.deleteById(bookId);
    }
    
    /**
     * Get all books
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    /**
     * Get book by ID
     */
    public Optional<Book> getBookById(Long bookId) {
        return bookRepository.findById(bookId);
    }
    
    /**
     * Get book by ISBN
     */
    public Optional<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }
    
    /**
     * Search books by title
     */
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }
    
    /**
     * Search books by author
     */
    public List<Book> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }
    
    /**
     * Search books by category
     */
    public List<Book> searchBooksByCategory(String category) {
        return bookRepository.findByCategory(category);
    }
    
    /**
     * Search books by keyword (title, author, or category)
     */
    public List<Book> searchBooks(String keyword) {
        return bookRepository.searchBooks(keyword);
    }
    
    /**
     * Get all available books
     */
    public List<Book> getAvailableBooks() {
        return bookRepository.findAvailableBooks();
    }
    
    /**
     * Update book availability
     */
    public void updateBookAvailability(Long bookId, int change) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            book.setAvailableCopies(book.getAvailableCopies() + change);
            
            // Update status based on availability
            if (book.getAvailableCopies() > 0) {
                book.setStatus(BookStatus.AVAILABLE);
            } else {
                book.setStatus(BookStatus.BORROWED);
            }
            
            bookRepository.save(book);
        }
    }
}
