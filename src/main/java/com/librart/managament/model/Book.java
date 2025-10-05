package com.librart.managament.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "books")
public class Book {

    @Id
    private String bookId;

    @Column(name = "book_name", nullable = false)
    private String title;

    @Column(name = "book_author", nullable = false)
    private String author;

    private int totalCopies;

    private int availableCopies;

    private String description;

    private LocalDate publishDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<IssuedBook>issuedBooks;

    @PrePersist
    void onCreate(){
        this.bookId = UUID.randomUUID().toString();
    }
}
