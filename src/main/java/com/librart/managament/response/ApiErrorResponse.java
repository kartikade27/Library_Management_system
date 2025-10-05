package com.librart.managament.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.Instant;
import java.util.Map;

@Setter
@ToString
@Getter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {

    private final Instant timestamp;

    private final int status;

    private final String error;

    private final String message;

    private final String path;

    @Builder.Default
    private final Map<String , String>validationErrors =null;
}
