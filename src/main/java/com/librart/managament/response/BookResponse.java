package com.librart.managament.response;

import com.librart.managament.dto.CategoryDTO;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {

    private String bookId;

    private String title;

    private String author;

    private int totalCopies;

    private int availableCopies;

    private String description;

    private LocalDate publishDate;

    private CategoryDTO category;
}
