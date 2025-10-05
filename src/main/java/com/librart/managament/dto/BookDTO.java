package com.librart.managament.dto;

import com.librart.managament.model.Category;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {

    @NotBlank(message = "Title is required!")
    private String title;
    @NotBlank(message = "Author name is required!")
    private String author;

    @Min(value = 1, message = "Total copies must be at least 1")
    private int totalCopies;

    @Size(max = 1000, message = "Description can't exceed 1000 characters")
    private String description;

    @NotNull(message = "Published date is required")
    @PastOrPresent(message = "Published date cannot be in the future")
    private LocalDate publishedDate;

    private CategoryDTO category;
}
