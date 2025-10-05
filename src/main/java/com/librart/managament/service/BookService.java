package com.librart.managament.service;

import com.librart.managament.dto.BookDTO;
import com.librart.managament.response.ApiResponse;
import com.librart.managament.response.BookResponse;
import com.librart.managament.utility.PaginateResponse;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;

public interface BookService {

    BookResponse addBook(BookDTO bookDTO, String categoryId);

    BookResponse updateBook(BookDTO bookDTO, String bookId);

    BookResponse findSingleBookById(String bookId);

    PaginateResponse<BookResponse> findAllBooks(int page, int size);



    PaginateResponse<BookResponse> getBookByCategory(String categoryId, int page, int size);

    List<BookResponse> searchBookByTitle(String title);

    List<BookResponse> searchBookByAuthor(String author);

    List<BookResponse> getAvailableCopiesGreaterThan(int count);

    List<BookResponse> getBookPublishDateAfter(LocalDate date);

    ApiResponse deleteBook(String bookId);


}
