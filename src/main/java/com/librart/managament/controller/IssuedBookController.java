package com.librart.managament.controller;

import com.librart.managament.response.BookIssuedResponse;
import com.librart.managament.response.IssuedBookRequest;
import com.librart.managament.response.ReturnBookRequest;
import com.librart.managament.service.IssuedBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issuedBooks")
@RequiredArgsConstructor
public class IssuedBookController {

    private final IssuedBookService issuedBookService;

    @PostMapping("/issueBook")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<BookIssuedResponse> issueBook(@RequestParam String userId, @RequestParam IssuedBookRequest request) {
        BookIssuedResponse bookIssuedResponse = this.issuedBookService.issueBook(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookIssuedResponse);
    }

    @PostMapping("/returnBook")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<BookIssuedResponse> returnBook(@RequestParam String userId, @RequestParam ReturnBookRequest request) {
        BookIssuedResponse bookIssuedResponse = this.issuedBookService.returnBook(userId, request);
        return ResponseEntity.ok(bookIssuedResponse);
    }

    @GetMapping("/getIssuedBookByUser/{userId}")
    @PreAuthorize("securityUtils.isAdmin() or securityUtils.isLibrarian() or securityUtils.isSelf(#userId)")
    public ResponseEntity<List<BookIssuedResponse>> getIssuedBooksByUser(@PathVariable String userId) {
        List<BookIssuedResponse> issuedBooksByUser = this.issuedBookService.getIssuedBooksByUser(userId);
        return ResponseEntity.ok(issuedBooksByUser);
    }

    @GetMapping("/getActiveIssuedBooks/{userId}")
    @PreAuthorize("securityUtils.isAdmin() or securityUtils.isLibrarian() or securityUtils.isSelf(#userId)")
    public ResponseEntity<List<BookIssuedResponse>> getActiveIssuedBooks(@PathVariable String userId) {
        List<BookIssuedResponse> activeIssuedBooks = this.issuedBookService.getActiveIssuedBooks(userId);
        return ResponseEntity.ok(activeIssuedBooks);
    }

    @GetMapping("/getOverdueBooksByUser/{userId}")
    @PreAuthorize("securityUtils.isAdmin() or securityUtils.isLibrarian() or securityUtils.isSelf(#userId)")
    public ResponseEntity<List<BookIssuedResponse>> getOverdueBooksByUser(@PathVariable String userId) {
        List<BookIssuedResponse> overdueBooksByUser = this.issuedBookService.getOverdueBooksByUser(userId);
        return ResponseEntity.ok(overdueBooksByUser);
    }

    @GetMapping("/getAllIssuedBooks")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<List<BookIssuedResponse>> getAllIssuedBooks() {
        List<BookIssuedResponse> allIssuedBooks = this.issuedBookService.getAllIssuedBooks();
        return ResponseEntity.ok(allIssuedBooks);
    }

    @GetMapping("/getAllOverdueBooks")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<List<BookIssuedResponse>> getAllOverdueBooks() {
        List<BookIssuedResponse> allOverdueBooks = this.issuedBookService.getAllOverdueBooks();
        return ResponseEntity.ok(allOverdueBooks);
    }
}
