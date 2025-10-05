package com.librart.managament.controller;

import com.librart.managament.dto.BookDTO;
import com.librart.managament.response.ApiResponse;
import com.librart.managament.response.BookResponse;
import com.librart.managament.service.BookService;
import com.librart.managament.utility.PaginateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/addBook/{categoryId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<BookResponse> addBook(@Valid @RequestBody BookDTO bookDTO, @PathVariable String categoryId) {
        BookResponse bookResponse = this.bookService.addBook(bookDTO, categoryId);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookResponse);
    }

    @PutMapping("/updateBook/{bookId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<BookResponse> updateBook(@Valid @RequestBody BookDTO bookDTO, @PathVariable String bookId) {
        BookResponse bookResponse = this.bookService.updateBook(bookDTO, bookId);
        return ResponseEntity.ok(bookResponse);
    }

    @GetMapping("/singleBookById/{bookId}")
    @PreAuthorize("hasAnyRole('ADMIN' , 'LIBRARIAN' , 'USER')")
    public ResponseEntity<BookResponse> findSingleBookById(@PathVariable String bookId) {
        BookResponse singleBookById = this.bookService.findSingleBookById(bookId);
        return ResponseEntity.ok(singleBookById);
    }

    @GetMapping("/allBooks")
    @PreAuthorize("hasAnyRole('ADMIN' , 'LIBRARIAN' , 'USER')")
    public ResponseEntity<PaginateResponse<BookResponse>> findAllBooks(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {
        PaginateResponse<BookResponse> allBooks = this.bookService.findAllBooks(page, size);
        return ResponseEntity.ok(allBooks);
    }

    @GetMapping("/getBookByCategory/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN' , 'LIBRARIAN' , 'USER')")
    public ResponseEntity<PaginateResponse<BookResponse>> getBookByCategory(@PathVariable String categoryId,
                                                                            @RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "10") int size) {


        PaginateResponse<BookResponse> bookByCategory = this.bookService.getBookByCategory(categoryId, page, size);
        return ResponseEntity.ok(bookByCategory);
    }

    @GetMapping("/searchBookByTitle")
    @PreAuthorize("hasAnyRole('ADMIN' , 'LIBRARIAN' , 'USER')")
    public ResponseEntity<List<BookResponse>> searchBookByTitle(@RequestParam String title) {
        List<BookResponse> bookResponses = this.bookService.searchBookByTitle(title);
        return ResponseEntity.ok(bookResponses);
    }

    @GetMapping("/searchBookByAuthor")
    @PreAuthorize("hasAnyRole('ADMIN' , 'LIBRARIAN' , 'USER')")
    public ResponseEntity<List<BookResponse>> searchBookByAuthor(@RequestParam String author) {
        List<BookResponse> bookResponses = this.bookService.searchBookByAuthor(author);
        return ResponseEntity.ok(bookResponses);
    }

    @GetMapping("/getAvailableCopiesGreaterThan")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<List<BookResponse>> getAvailableCopiesGreaterThan(@RequestParam int count) {
        List<BookResponse> availableCopiesGreaterThan = this.bookService.getAvailableCopiesGreaterThan(count);
        return ResponseEntity.ok(availableCopiesGreaterThan);
    }


    @GetMapping("/getBookPublishDateAfter")
    @PreAuthorize("hasRole('ADMIN ') or hasRole('LIBRARIAN')")
    public ResponseEntity<List<BookResponse>> getBookPublishDateAfter(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<BookResponse> bookPublishDateAfter = this.bookService.getBookPublishDateAfter(date);
        return ResponseEntity.ok(bookPublishDateAfter);
    }

    @DeleteMapping("/deleteBook/{bookId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteBook(@PathVariable String bookId) {
        ApiResponse apiResponse = this.bookService.deleteBook(bookId);
        return ResponseEntity.ok(apiResponse);
    }
}
