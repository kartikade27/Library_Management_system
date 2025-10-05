package com.librart.managament.service;

import com.librart.managament.response.BookIssuedResponse;
import com.librart.managament.response.IssuedBookRequest;
import com.librart.managament.response.ReturnBookRequest;

import java.util.List;

public interface IssuedBookService {

    BookIssuedResponse issueBook(String userId , IssuedBookRequest request);

    BookIssuedResponse returnBook(String userId , ReturnBookRequest request);

    List<BookIssuedResponse>getIssuedBooksByUser(String userId);

    List<BookIssuedResponse>getActiveIssuedBooks(String userId);

    List<BookIssuedResponse>getOverdueBooksByUser(String userId);

    List<BookIssuedResponse>getAllIssuedBooks();

    List<BookIssuedResponse>getAllOverdueBooks();

}
