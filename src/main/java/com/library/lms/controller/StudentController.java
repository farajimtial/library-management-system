package com.library.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.library.lms.entity.Book;
import com.library.lms.entity.BorrowingRecord;
import com.library.lms.entity.User;
import com.library.lms.service.BookService;
import com.library.lms.service.BorrowingService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BorrowingService borrowingService;

    /** Dashboard */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/auth/login";

        model.addAttribute("user", user);

        List<BorrowingRecord> activeBorrowings = borrowingService.getActiveBorrowingsByStudent(user.getUserId());
        if (activeBorrowings == null) activeBorrowings = List.of();
        model.addAttribute("activeBorrowings", activeBorrowings);

        return "student/dashboard";
    }

    /** Search Books Page */
    @GetMapping("/search-books")
    public String searchBooksPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/auth/login";

        List<Book> books = bookService.getAllBooks();
        if (books == null) books = List.of();

        model.addAttribute("books", books);
        model.addAttribute("user", user);

        return "student/search-books";
    }

    /** Search Books by keyword */
    @GetMapping("/search")
    public String searchBooks(@RequestParam String keyword, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/auth/login";

        List<Book> books = bookService.searchBooks(keyword);
        if (books == null) books = List.of();

        model.addAttribute("books", books);
        model.addAttribute("keyword", keyword);
        model.addAttribute("user", user);

        return "student/search-books";
    }

    /** View Borrowed Books */
    @GetMapping("/borrowed-books")
    public String viewBorrowedBooks(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/auth/login";

        List<BorrowingRecord> borrowingRecords = borrowingService.getBorrowingRecordsByStudent(user.getUserId());
        if (borrowingRecords == null) borrowingRecords = List.of();

        model.addAttribute("borrowingRecords", borrowingRecords);
        model.addAttribute("user", user);

        return "student/borrowed-books";
    }

    /** Borrow Book AJAX */
    @PostMapping("/borrow/{bookId}")
    @ResponseBody
    public String borrowBook(@PathVariable Long bookId, @RequestParam(defaultValue = "14") Integer days,
                             HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) return "error:Please login first";

            borrowingService.borrowBook(user.getUserId(), bookId, days);
            return "success:Book borrowed successfully";
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }
    }
}
