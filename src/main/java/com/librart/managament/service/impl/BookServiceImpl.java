package com.librart.managament.service.impl;

import com.librart.managament.dto.BookDTO;
import com.librart.managament.dto.CategoryDTO;
import com.librart.managament.exception.ResourceAlreadyExistsException;
import com.librart.managament.exception.ResourceNotFoundException;
import com.librart.managament.model.Book;
import com.librart.managament.model.Category;
import com.librart.managament.repository.BookRepository;
import com.librart.managament.repository.CategoryRepository;
import com.librart.managament.response.ApiResponse;
import com.librart.managament.response.BookResponse;
import com.librart.managament.service.BookService;
import com.librart.managament.utility.PaginateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public BookResponse addBook(BookDTO bookDTO, String categoryId) {
        if (bookRepository.existsByTitleAndPublishDateAndAuthor(
                bookDTO.getTitle(), bookDTO.getPublishedDate(), bookDTO.getAuthor())) {
            throw new ResourceAlreadyExistsException("Book already exists!");
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));

        Book book = Book.builder()
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .totalCopies(bookDTO.getTotalCopies())
                .availableCopies(bookDTO.getTotalCopies())
                .description(bookDTO.getDescription())
                .publishDate(bookDTO.getPublishedDate())
                .category(category)
                .build();

        Book saved = bookRepository.save(book);
        return convertToBookResponse(saved);
    }

    @Override
    public BookResponse updateBook(BookDTO bookDTO, String bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found!"));

        Category category = categoryRepository.findById(bookDTO.getCategory().getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));

        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setTotalCopies(bookDTO.getTotalCopies());
        book.setPublishDate(bookDTO.getPublishedDate());
        book.setDescription(bookDTO.getDescription());
        book.setCategory(category);

        Book updated = bookRepository.save(book);
        return convertToBookResponse(updated);
    }

    @Override
    public BookResponse findSingleBookById(String bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found!"));
        return convertToBookResponse(book);
    }

    @Override
    public PaginateResponse<BookResponse> findAllBooks(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("title").ascending());
        Page<Book> bookPage = bookRepository.findAll(pageRequest);

        List<BookResponse> responses = bookPage.getContent().stream()
                .map(this::convertToBookResponse)
                .collect(Collectors.toList());

        return PaginateResponse.<BookResponse>builder()
                .content(responses)
                .pageNumber(bookPage.getNumber())
                .pageSize(bookPage.getSize())
                .totalElements(bookPage.getTotalElements())
                .totalPages(bookPage.getTotalPages())
                .build();
    }

    @Override
    public PaginateResponse<BookResponse> getBookByCategory(String categoryId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("title").ascending());
        Page<Book> bookPage = bookRepository.findByCategory_CategoryId(categoryId, pageRequest);

        List<BookResponse> responses = bookPage.getContent().stream()
                .map(this::convertToBookResponse)
                .collect(Collectors.toList());

        return PaginateResponse.<BookResponse>builder()
                .content(responses)
                .pageNumber(bookPage.getNumber())
                .pageSize(bookPage.getSize())
                .totalElements(bookPage.getTotalElements())
                .totalPages(bookPage.getTotalPages())
                .build();
    }

    @Override
    public List<BookResponse> searchBookByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::convertToBookResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> searchBookByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author).stream()
                .map(this::convertToBookResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> getAvailableCopiesGreaterThan(int count) {
        return bookRepository.findByAvailableCopiesGreaterThan(count).stream()
                .map(this::convertToBookResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> getBookPublishDateAfter(LocalDate date) {
        return bookRepository.findByPublishDateAfter(date).stream()
                .map(this::convertToBookResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ApiResponse deleteBook(String bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found!"));
        bookRepository.delete(book);
        return ApiResponse.builder()
                .message("Book deleted successfully")
                .build();
    }

    private BookResponse convertToBookResponse(Book book) {
        return BookResponse.builder()
                .bookId(book.getBookId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .description(book.getDescription())
                .publishDate(book.getPublishDate())
                .category(convertToCategoryDTO(book.getCategory()))
                .build();
    }

    private CategoryDTO convertToCategoryDTO(Category category) {
        if (category == null) return null;
        return CategoryDTO.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .build();
    }
}
