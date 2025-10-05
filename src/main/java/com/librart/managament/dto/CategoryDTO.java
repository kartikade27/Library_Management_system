package com.librart.managament.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {

    private  String categoryId;

    @NotBlank(message = "Category is required!")
    private String categoryName;
}
