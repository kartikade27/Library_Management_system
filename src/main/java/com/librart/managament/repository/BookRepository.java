package com.librart.managament.repository;

import com.librart.managament.model.Book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, String> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByAvailableCopiesGreaterThan(int count);

    List<Book> findByPublishDateAfter(LocalDate date);
    
    boolean existsByTitleAndPublishDateAndAuthor(String title, LocalDate publishDate, String author);

    Page<Book> findByCategory_CategoryId(String categoryId , Pageable pageable);
}
