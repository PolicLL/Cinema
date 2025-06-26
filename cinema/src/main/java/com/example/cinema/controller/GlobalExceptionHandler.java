package com.example.cinema.controller;

import com.example.cinema.dto.ErrorResponse;
import com.example.cinema.exception.ActorNotFoundException;
import com.example.cinema.exception.MovieNotFoundException;
import com.example.cinema.exception.MovieWithNameExistsException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleValidationError(MethodArgumentNotValidException ex, HttpServletRequest request) {
    String message = ex.getBindingResult().getAllErrors().stream()
        .map(ObjectError::getDefaultMessage)
        .collect(Collectors.joining("; "));

    return new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.BAD_REQUEST.value(),
        "Validation Error",
        message,
        request.getRequestURI()
    );
  }

  @ExceptionHandler(MovieWithNameExistsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleMovieWithNameExistsException(MovieWithNameExistsException ex, HttpServletRequest request) {
    return ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build();
  }

  @ExceptionHandler(ActorNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleActorNotFoundException(ActorNotFoundException ex, HttpServletRequest request) {
    return ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build();
  }

  @ExceptionHandler(MovieNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleMovieNotFoundException(MovieNotFoundException ex, HttpServletRequest request) {
    return ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build();
  }
}