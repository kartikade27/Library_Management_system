package com.librart.managament.service.impl;

import com.librart.managament.exception.ResourceAlreadyExistsException;
import com.librart.managament.exception.ResourceNotFoundException;
import com.librart.managament.model.Book;
import com.librart.managament.model.IssuedBook;
import com.librart.managament.repository.BookRepository;
import com.librart.managament.repository.IssuedBookRepository;
import com.librart.managament.repository.UserRepository;
import com.librart.managament.response.BookIssuedResponse;
import com.librart.managament.response.IssuedBookRequest;
import com.librart.managament.response.ReturnBookRequest;
import com.librart.managament.service.IssuedBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class IssuedBookServiceImpl implements IssuedBookService {

    private final IssuedBookRepository issuedBookRepository;

    private final UserRepository userRepository;

    private final BookRepository bookRepository;

    @Override
    public BookIssuedResponse issueBook(String userId, IssuedBookRequest request) {
        this.issuedBookRepository.findByUser_UserIdAndBook_BookIdAndReturnDateIsNull(userId, request.getBookId())
                .ifPresent(b -> {
                    throw new ResourceAlreadyExistsException("Book already issued");
                });

        Book book = this.bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found!"));

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("Book has no available copies!");
        }
        IssuedBook issuedBook = IssuedBook.builder()
                .user(this.userRepository.findById(userId).orElseThrow())
                .book(book)
                .issuedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(7)).build();

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        this.bookRepository.save(book);

        IssuedBook saved = this.issuedBookRepository.save(issuedBook);
        return mapToResponse(saved);
    }

    @Override
    public BookIssuedResponse returnBook(String userId, ReturnBookRequest request) {
        IssuedBook issuedBook = this.issuedBookRepository.findByUser_UserIdAndBook_BookIdAndReturnDateIsNull(userId, request.getBookId())
                .orElseThrow(() -> new ResourceAlreadyExistsException("Book not issued or already returned!"));
        issuedBook.setReturnDate(LocalDate.now());
        if (issuedBook.getReturnDate().isAfter(issuedBook.getIssuedDate())) {
            long daysLate = issuedBook.getReturnDate().toEpochDay() - issuedBook.getDueDate().toEpochDay();
            issuedBook.setFineAmount((double) daysLate * 5);
        }
        Book book = issuedBook.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        this.bookRepository.save(book);
        IssuedBook saved = this.issuedBookRepository.save(issuedBook);
        return mapToResponse(saved);
    }

    @Override
    public List<BookIssuedResponse> getIssuedBooksByUser(String userId) {
        List<IssuedBook> byUserUserId = this.issuedBookRepository.findByUser_UserId(userId);
        List<BookIssuedResponse> issuedBooks = byUserUserId.stream().map(this::mapToResponse).toList();
        return issuedBooks;
    }

    @Override
    public List<BookIssuedResponse> getActiveIssuedBooks(String userId) {
        List<IssuedBook> activeIssuedBooks = this.issuedBookRepository.findByUser_UserIdAndReturnDateIsNull(userId);
        List<BookIssuedResponse> activeIssuedBook = activeIssuedBooks.stream().map(this::mapToResponse).toList();

        return activeIssuedBook;
    }

    @Override
    public List<BookIssuedResponse> getOverdueBooksByUser(String userId) {
        List<IssuedBook> getOverdueBooksByUsers = this.issuedBookRepository.findByUser_UserIdAndDueDateBeforeAndReturnDateIsNull(userId, LocalDate.now());
        List<BookIssuedResponse> getOverdueBooksByUser = getOverdueBooksByUsers.stream().map(this::mapToResponse).toList();
        return getOverdueBooksByUser;
    }

    @Override
    public List<BookIssuedResponse> getAllIssuedBooks() {
        return this.issuedBookRepository.findAll()
                .stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<BookIssuedResponse> getAllOverdueBooks() {
        return this.issuedBookRepository.findByDueDateBeforeAndReturnDateIsNull(LocalDate.now())
                .stream().map(this::mapToResponse).toList();
    }

    private BookIssuedResponse mapToResponse(IssuedBook issued) {
        BookIssuedResponse dto = new BookIssuedResponse();
        dto.setId(issued.getIssuedBookId());
        dto.setBookId(issued.getBook().getBookId());
        dto.setBookTitle(issued.getBook().getTitle());
        dto.setIssueDate(issued.getIssuedDate());
        dto.setDueDate(issued.getDueDate());
        dto.setReturnDate(issued.getReturnDate());
        dto.setFineAmount(issued.getFineAmount());
        return dto;
    }
}
