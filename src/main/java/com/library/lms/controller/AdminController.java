package com.library.lms.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.library.lms.entity.Book;
import com.library.lms.entity.BorrowingRecord;
import com.library.lms.entity.Librarian;
import com.library.lms.entity.User;
import com.library.lms.enums.BorrowStatus;
import com.library.lms.service.BookService;
import com.library.lms.service.BorrowingService;
import com.library.lms.service.UserService;

import jakarta.servlet.http.HttpSession;

/**
 * AdminController - Handles admin-specific operations
 * Manages users, librarians, and system reports
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private BorrowingService borrowingService;
    
    /**
     * Admin Dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        
        // Get statistics
        List<User> allUsers = userService.getAllUsers();
        List<Book> allBooks = bookService.getAllBooks();
        List<BorrowingRecord> allBorrowings = borrowingService.getAllBorrowingRecords();
        List<BorrowingRecord> activeBorrowings = allBorrowings.stream()
                .filter(r -> r.getStatus() == BorrowStatus.ACTIVE)
                .collect(Collectors.toList());
        List<BorrowingRecord> overdueRecords = borrowingService.getOverdueRecords();
        
        model.addAttribute("totalUsers", allUsers.size());
        model.addAttribute("totalBooks", allBooks.size());
        model.addAttribute("totalBorrowings", allBorrowings.size());
        model.addAttribute("activeBorrowings", activeBorrowings.size());
        model.addAttribute("overdueCount", overdueRecords.size());
        
        // Calculate total available copies
        int totalAvailable = allBooks.stream()
                .mapToInt(Book::getAvailableCopies)
                .sum();
        model.addAttribute("totalAvailable", totalAvailable);
        
        return "admin/dashboard";
    }
    
    /**
     * View System Reports
     */
    @GetMapping("/reports")
    public String viewReports(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        
        // Get all data
        List<Book> allBooks = bookService.getAllBooks();
        List<BorrowingRecord> allBorrowings = borrowingService.getAllBorrowingRecords();
        List<User> allUsers = userService.getAllUsers();
        
        // Book Statistics
        Map<String, Long> booksByCategory = allBooks.stream()
                .collect(Collectors.groupingBy(Book::getCategory, Collectors.counting()));
        
        // Borrowing Statistics
        long activeBorrowings = allBorrowings.stream()
                .filter(r -> r.getStatus() == BorrowStatus.ACTIVE)
                .count();
        
        long returnedBorrowings = allBorrowings.stream()
                .filter(r -> r.getStatus() == BorrowStatus.RETURNED)
                .count();
        
        long overdueBorrowings = borrowingService.getOverdueRecords().size();
        
        // Most borrowed books
        Map<Long, Long> borrowCountByBook = allBorrowings.stream()
                .collect(Collectors.groupingBy(
                    r -> r.getBook().getBookId(), 
                    Collectors.counting()
                ));
        
        List<Book> topBorrowedBooks = borrowCountByBook.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(5)
                .map(entry -> bookService.getBookById(entry.getKey()).orElse(null))
                .filter(book -> book != null)
                .collect(Collectors.toList());
        
        // User Statistics
        long totalStudents = allUsers.stream()
                .filter(u -> u.getRole() == com.library.lms.enums.Role.STUDENT)
                .count();
        
        long totalLibrarians = allUsers.stream()
                .filter(u -> u.getRole() == com.library.lms.enums.Role.LIBRARIAN)
                .count();
        
        // Add to model
        model.addAttribute("booksByCategory", booksByCategory);
        model.addAttribute("activeBorrowings", activeBorrowings);
        model.addAttribute("returnedBorrowings", returnedBorrowings);
        model.addAttribute("overdueBorrowings", overdueBorrowings);
        model.addAttribute("topBorrowedBooks", topBorrowedBooks);
        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("totalLibrarians", totalLibrarians);
        model.addAttribute("totalBooks", allBooks.size());
        
        // Calculate percentages
        int totalCopies = allBooks.stream().mapToInt(Book::getTotalCopies).sum();
        int availableCopies = allBooks.stream().mapToInt(Book::getAvailableCopies).sum();
        double utilizationRate = totalCopies > 0 ? 
                ((totalCopies - availableCopies) * 100.0 / totalCopies) : 0;
        
        model.addAttribute("totalCopies", totalCopies);
        model.addAttribute("availableCopies", availableCopies);
        model.addAttribute("utilizationRate", String.format("%.1f", utilizationRate));
        
        return "admin/reports";
    }
    
    /**
     * View All Borrowing Records
     */
    @GetMapping("/borrowing-records")
    public String viewBorrowingRecords(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        
        List<BorrowingRecord> allRecords = borrowingService.getAllBorrowingRecords();
        model.addAttribute("records", allRecords);
        
        return "admin/borrowing-records";
    }
    
    /**
     * Manage Users Page
     */
    @GetMapping("/manage-users")
    public String manageUsers(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("user", user);
        
        return "admin/manage-users";
    }
    
    /**
     * Add Librarian Page
     */
    @GetMapping("/add-librarian")
    public String addLibrarianPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        return "admin/add-librarian";
    }
    
    /**
     * Add Librarian
     */
    @PostMapping("/add-librarian")
    public String addLibrarian(@ModelAttribute Librarian librarian, Model model) {
        try {
            userService.registerLibrarian(librarian);
            return "redirect:/admin/manage-users?success=Librarian added successfully";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "admin/add-librarian";
        }
    }
    
    /**
     * Delete User
     */
    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return "redirect:/admin/manage-users?success=User deleted successfully";
        } catch (Exception e) {
            return "redirect:/admin/manage-users?error=" + e.getMessage();
        }
    }
}