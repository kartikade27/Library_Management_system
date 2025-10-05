package com.librart.managament.response;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OverdueBookResponse {

    private String issuedId;

    private String userId;

    private String userName;

    private String bookId;

    private String bookName;

    private LocalDate dueDate;

    private double fineAmount;
}
