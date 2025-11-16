package com.library.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.library.lms.entity.Book;
import com.library.lms.entity.BorrowingRecord;
import com.library.lms.entity.User;
import com.library.lms.service.BookService;
import com.library.lms.service.BorrowingService;

import jakarta.servlet.http.HttpSession;

/**
 * LibrarianController - Handles librarian-specific operations
 * Manages book CRUD operations, borrowing, and returns
 */
@Controller
@RequestMapping("/librarian")
public class LibrarianController {
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private BorrowingService borrowingService;
    
    /**
     * Librarian Dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        
        // Get statistics
        List<Book> allBooks = bookService.getAllBooks();
        List<BorrowingRecord> allBorrowings = borrowingService.getAllBorrowingRecords();
        List<BorrowingRecord> overdueRecords = borrowingService.getOverdueRecords();
        
        model.addAttribute("totalBooks", allBooks.size());
        model.addAttribute("totalBorrowings", allBorrowings.size());
        model.addAttribute("overdueCount", overdueRecords.size());
        
        return "librarian/dashboard";
    }
    
    /**
     * Manage Books Page
     */
    @GetMapping("/manage-books")
    public String manageBooks(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        model.addAttribute("user", user);
        
        return "librarian/manage-books";
    }
    
    /**
     * Add Book Page
     */
    @GetMapping("/add-book")
    public String addBookPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        return "librarian/add-book";
    }
    
    /**
     * Add Book
     */
    @PostMapping("/add-book")
    public String addBook(@ModelAttribute Book book, Model model) {
        try {
            bookService.addBook(book);
            return "redirect:/librarian/manage-books?success=Book added successfully";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "librarian/add-book";
        }
    }
    
    /**
     * Edit Book Page
     */
    @GetMapping("/edit-book/{id}")
    public String editBookPage(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        Book book = bookService.getBookById(id).orElse(null);
        if (book == null) {
            return "redirect:/librarian/manage-books?error=Book not found";
        }
        
        model.addAttribute("book", book);
        model.addAttribute("user", user);
        
        return "librarian/edit-book";
    }
    
    /**
     * Update Book
     */
    @PostMapping("/edit-book/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book book) {
        try {
            book.setBookId(id);
            bookService.updateBook(book);
            return "redirect:/librarian/manage-books?success=Book updated successfully";
        } catch (Exception e) {
            return "redirect:/librarian/edit-book/" + id + "?error=" + e.getMessage();
        }
    }
    
    /**
     * Delete Book
     */
    @GetMapping("/delete-book/{id}")
    public String deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return "redirect:/librarian/manage-books?success=Book deleted successfully";
        } catch (Exception e) {
            return "redirect:/librarian/manage-books?error=" + e.getMessage();
        }
    }
    
    /**
     * Borrow/Return Management Page
     */
    @GetMapping("/borrow-return")
    public String borrowReturnPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        List<BorrowingRecord> activeRecords = borrowingService.getAllBorrowingRecords();
        model.addAttribute("records", activeRecords);
        model.addAttribute("user", user);
        
        return "librarian/borrow-return";
    }
    
    /**
     * Process Book Return
     */
    @PostMapping("/return/{recordId}")
    @ResponseBody
    public String returnBook(@PathVariable Long recordId) {
        try {
            borrowingService.returnBook(recordId);
            return "success:Book returned successfully";
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }
    }
}

