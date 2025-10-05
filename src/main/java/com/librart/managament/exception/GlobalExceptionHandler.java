package com.librart.managament.exception;


import com.librart.managament.response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.View;

import javax.naming.AuthenticationException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final View error;

    public GlobalExceptionHandler(View error) {
        this.error = error;
    }

    private ResponseEntity<ApiErrorResponse>buildErrorResponse(HttpStatus status , String message , String path){
        ApiErrorResponse error = ApiErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .build();

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse>handleResourceNotFoundException(ResourceNotFoundException ex ,
                                                                            HttpServletRequest request){
        return buildErrorResponse(HttpStatus.NOT_FOUND , ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse>resourceAlreadyExistsException(ResourceAlreadyExistsException ex ,
                                                                          HttpServletRequest request){
        return buildErrorResponse(HttpStatus.CONFLICT , ex.getMessage() , request.getRequestURI());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse>handleBadcreditionalException(BadCredentialsException ex , HttpServletRequest request){
        return buildErrorResponse(HttpStatus.UNAUTHORIZED , ex.getMessage() , request.getRequestURI());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse>handleAuthenticationException(AuthenticationException ex , HttpServletRequest request){
        return buildErrorResponse(HttpStatus.UNAUTHORIZED , ex.getMessage() , request.getRequestURI());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse>handleAccessDeniedException(AccessDeniedException ex , HttpServletRequest request){
        return buildErrorResponse(HttpStatus.FORBIDDEN , ex.getMessage() , request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse>handleMethodArgumentNotValidException(MethodArgumentNotValidException ex ,
                                                                                 HttpServletRequest request){
        Map<String , String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) ->{
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            validationErrors.put(field , message);
        });
        ApiErrorResponse errors = ApiErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation Failed " + validationErrors.size() + " field(s).")
                .path(request.getRequestURI())
                .validationErrors(validationErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse>handleGenericException(Exception ex , HttpServletRequest request){
        return  buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR ,ex.getMessage() , request.getRequestURI());
    }
}
