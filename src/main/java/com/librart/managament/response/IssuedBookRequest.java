package com.librart.managament.response;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssuedBookRequest {

    private String bookId;
}
