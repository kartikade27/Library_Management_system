package com.librart.managament.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "issued_books")
public class IssuedBook {

    @Id
    private String issuedBookId;

    private LocalDate issuedDate;

    private LocalDate dueDate;

    private LocalDate returnDate;

    private Double fineAmount;

    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id" , nullable = false)
    private Book book;

    @PrePersist
    void onCreate(){
        this.issuedBookId = UUID.randomUUID().toString();
    }
}
