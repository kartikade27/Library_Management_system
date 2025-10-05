package com.librart.managament.response;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookIssuedResponse {

    private String id;

    private String bookId;

    private String bookTitle;

    private LocalDate issueDate;

    private LocalDate dueDate;

    private LocalDate returnDate;

    private double fineAmount;


}
